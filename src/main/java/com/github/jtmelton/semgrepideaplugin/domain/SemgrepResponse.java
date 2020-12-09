package com.github.jtmelton.semgrepideaplugin.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class SemgrepResponse {

    public Collection<Result> getResults() {
        return results;
    }

    public void setResults(Collection<Result> results) {
        this.results = results;
    }

    public Collection<Error> getErrors() {
        return errors;
    }

    public void setErrors(Collection<Error> errors) {
        this.errors = errors;
    }

    private Collection<Result> results;
    private Collection<Error> errors;

    public class Result {
        @JsonProperty("check_id")
        private String checkId;
        private String path;
        private Location start;
        private Location end;
        private Extra extra;

        public String getCheckId() {
            return checkId;
        }

        public void setCheckId(String checkId) {
            this.checkId = checkId;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Location getStart() {
            return start;
        }

        public void setStart(Location start) {
            this.start = start;
        }

        public Location getEnd() {
            return end;
        }

        public void setEnd(Location end) {
            this.end = end;
        }

        public Extra getExtra() {
            return extra;
        }

        public void setExtra(Extra extra) {
            this.extra = extra;
        }
    }

    public class Location {
        private int line;
        private int col;

        public int getLine() {
            return line;
        }

        public void setLine(int line) {
            this.line = line;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }
    }

    public class Extra {
        private String message;
        private Metavars metavars;
        private Metadata metadata;
        private String severity;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Metavars getMetavars() {
            return metavars;
        }

        public void setMetavars(Metavars metavars) {
            this.metavars = metavars;
        }

        public Metadata getMetadata() {
            return metadata;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }
    }

    public class Metavars {

    }

    public class Metadata {

    }

    public class Error {

    }
}


