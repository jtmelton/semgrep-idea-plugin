package com.github.jtmelton.semgrepideaplugin.analysis;

import com.github.jtmelton.semgrepideaplugin.domain.SemgrepResponse;
import com.github.jtmelton.semgrepideaplugin.settings.AppSettingsComponent;
import com.google.gson.Gson;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SemgrepExecutor {
    private static final Logger LOGGER = Logger.getInstance(SemgrepExecutor.class.getName());

    private static final String DEFAULT_FILE_EXTENSION = "unknown";

    private static PropertiesComponent properties = PropertiesComponent.getInstance();

    static SemgrepResponse execute(@NotNull final Editor editor) {
        Path tmp;
        String extension = DEFAULT_FILE_EXTENSION;
        try {
            // https://intellij-support.jetbrains.com/hc/en-us/community/posts/206754735-Find-out-the-file-name-for-a-Document-or-Editor-instance
            // Not all Documents have an associated file name. For those that do, you can
            // call FileDocumentManager.getFile(), and if it returns not null, call VirtualFile.getPath()
            // on the returned file.
            VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if(virtualFile != null) {
                String virtualPath = virtualFile.getPath();
                extension = getFileExtension(virtualPath);
            }
            tmp = Files.createTempFile(null, extension);
        } catch (IOException ex) {
            LOGGER.error("There was a problem while preparing a temp file.", ex);
            throw new IllegalStateException(ex);
        }

        if (!isConfigurationValid()) {
            LOGGER.error("Invalid configuration file - skipping analysis");
            return new SemgrepResponse();
        }

        try {
            final Process process = createProcess(editor, tmp);
            if (process == null) {
                return new SemgrepResponse();
            }
            return getOutput(process, tmp);
        } finally {
            try {
                Files.delete(tmp);
            } catch (IOException ex) {
                LOGGER.error("There was a problem while deleting a temp file.", ex);
            }
        }
    }

    private static Process createProcess(final Editor editor, final Path tmp) {
        // ref. https://intellij-support.jetbrains.com/hc/en-us/community/posts/360004284939-How-to-trigger-ExternalAnnotator-running-immediately-after-saving-the-code-change-
        VirtualFile file = createSyncedFile(editor.getDocument(), tmp);

        if (file == null) {
            LOGGER.error("COULD NOT CREATE SYNC'ED FILE!: " + tmp);
            return null;
        }

        if (!file.isValid()) {
            LOGGER.debug("PSI File invalid: " + file.getCanonicalPath());
            return null;
        }

        // config is valid, try to actually execute semgrep against the file
        String analysisFilePath = file.getCanonicalPath();
        LOGGER.debug("Attempting to process file: " + analysisFilePath);

        ProcessBuilder processBuilder = new ProcessBuilder();

        // pass the binary 2 parameters, the rules file/dir, and the file to analyze
        processBuilder.command(getBinary(), getRules(), analysisFilePath);

        try {
            Process process = processBuilder.start();

            return process;
        } catch (IOException e) {
            LOGGER.error("There was a problem while trying to start semgrep execution.", e);
            throw new IllegalStateException(e);
        }
    }

    private static SemgrepResponse getOutput(final Process process, final Path tmp) {
        SemgrepResponse response = new SemgrepResponse();

        try {
            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            LOGGER.debug("Output of semgrep execution: " + output.toString());

            Gson gson = new Gson();
            response = gson.fromJson(output.toString(), SemgrepResponse.class);

            int exitVal = process.waitFor();
            LOGGER.debug("Exit value for semgrep: " + exitVal);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            LOGGER.error(e);
        }

        return response;
    }

    private static VirtualFile createSyncedFile(Document doc, Path tmp) {
        try {
            try(BufferedWriter out = Files.newBufferedWriter(tmp, StandardCharsets.UTF_8)) {
                out.write(doc.getText());
            }
            return LocalFileSystem.getInstance().findFileByPath(tmp.toString());
        } catch (IOException ex) {
            LOGGER.error("There was a problem while preparing a temp file.", ex);
            throw new IllegalStateException(ex);
        }
    }

    public static String getFileExtension(String fullName) {
        if(fullName == null) {
            return DEFAULT_FILE_EXTENSION;
        }

        String fileName = new File(fullName).getName();
        int index = fileName.lastIndexOf('.');
        return (index == -1) ? DEFAULT_FILE_EXTENSION : fileName.substring(index + 1);
    }

    private static boolean isConfigurationValid() {
        boolean isMisConfigured = false;

        if (!properties.isValueSet(AppSettingsComponent.SEMGREP_BINARY_LOCATION) ||
                getBinary() == null ||
                getBinary().isEmpty()) {
            LOGGER.error("**** ERROR: " + AppSettingsComponent.SEMGREP_BINARY_LOCATION + " not configured! ****");
        }

        if (!properties.isValueSet(AppSettingsComponent.SEMGREP_RULES_LOCATION) ||
                getRules() == null ||
                getRules().isEmpty()) {
            LOGGER.error("**** ERROR: " + AppSettingsComponent.SEMGREP_RULES_LOCATION + " not configured! ****");
        }

        if (isMisConfigured) {
            return false;       //valid
        }

        File binaryFile = new File(getBinary());
        if (binaryFile.exists() && binaryFile.isFile()) {
            // valid file
        } else {
            isMisConfigured = true;
            LOGGER.error("**** ERROR: " + getBinary() + " is not a valid file! ****");
        }

        File rulesFile = new File(getRules());
        if (rulesFile.exists() && (rulesFile.isFile() || rulesFile.isDirectory())) {
            // valid file
        } else {
            isMisConfigured = true;
            LOGGER.error("**** ERROR: " + getRules() + " is not a valid file or directory! ****");
        }

        if (isMisConfigured) {
            return false;       //valid
        }

        return true;    //valid
    }

    private static String getBinary() {
        return properties.getValue(AppSettingsComponent.SEMGREP_BINARY_LOCATION);
    }

    private static String getRules() {
        return properties.getValue(AppSettingsComponent.SEMGREP_RULES_LOCATION);
    }

}