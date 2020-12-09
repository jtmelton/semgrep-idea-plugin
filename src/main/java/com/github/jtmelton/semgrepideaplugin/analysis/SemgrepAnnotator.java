package com.github.jtmelton.semgrepideaplugin.analysis;

import com.github.jtmelton.semgrepideaplugin.domain.SemgrepResponse;
import com.github.jtmelton.semgrepideaplugin.utils.FileUtil;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// https://github.com/yoheimuta/intellij-protolint was helpful
public class SemgrepAnnotator extends ExternalAnnotator<Editor, SemgrepResponse> {
    private static final Logger LOGGER = Logger.getInstance(SemgrepAnnotator.class);

    public SemgrepAnnotator() {
        LOGGER.info("SemgrepAnnotator");
    }

    @Nullable
    @Override
    public Editor collectInformation(@NotNull PsiFile file, @NotNull Editor editor, boolean hasErrors) {
        LOGGER.debug("Called collectInformation");
        return editor;
    }

    @Nullable
    @Override
    public SemgrepResponse doAnnotate(Editor editor) {
        LOGGER.debug("Called doAnnotate");
        return SemgrepExecutor.execute(editor);
    }

    @Override
    public void apply(@NotNull PsiFile file, SemgrepResponse response, @NotNull AnnotationHolder holder) {
        LOGGER.debug("Called apply");
        Document document = PsiDocumentManager.getInstance(file.getProject()).getDocument(file);
        if (document == null) {
            LOGGER.warn("Not Found document");
            return;
        }

        for (SemgrepResponse.Result result : response.getResults()) {
            int lineNum = result.getStart().getLine();
            int colNum = result.getStart().getCol();

            String analysisFilePath = file.getVirtualFile().getCanonicalPath();
            int startOffset = FileUtil.getStartOffset(analysisFilePath, lineNum, colNum);

            PsiElement foundNode = file.findElementAt(startOffset);

            if (foundNode != null) {
                String severity = (result.getExtra().getSeverity() == null) ? "Unknown" : result.getExtra().getSeverity();
                String checkId = (result.getCheckId() == null) ? "N/A" : result.getCheckId();
                String message = (result.getExtra().getMessage() == null) ? "N/A" : result.getExtra().getMessage();

                String finding = "SemGrep finding (" + severity + ") " +
                        "for check id (" + checkId +
                        ") with message -> " + message;

                if("WARNING".equals(severity)) {
                    holder.createWarningAnnotation(foundNode, finding);
                } else if("ERROR".equals(severity)) {
                    holder.createErrorAnnotation(foundNode, finding);
                } else {
                    holder.createErrorAnnotation(foundNode, finding);
                }

            } else {
                LOGGER.debug("Could not find node with startOffset: " + startOffset +
                        " and lineNum: " + lineNum +
                        " and colNum: " + colNum +
                        " in analysisFilePath: " + analysisFilePath);
            }
        }
    }

    private boolean isProperRange(int startOffset, int endOffset) {
        return startOffset <= endOffset && startOffset >= 0;
    }
}
