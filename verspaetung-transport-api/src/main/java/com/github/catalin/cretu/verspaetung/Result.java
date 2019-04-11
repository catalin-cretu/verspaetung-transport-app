package com.github.catalin.cretu.verspaetung;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public interface Result<T> {

    static <T> Result<T> error(final String code, final String message) {
        return new Error<>(code, message);
    }

    static <T> Result<T> errors(final List<ErrorResult> errors) {
        return new Error<>(errors);
    }

    static <T> Ok<T> ok(final T result) {
        return new Ok<>(result);
    }

    /**
     * @return errors for Error Result or
     * @throws NoSuchElementException for OK Result
     */
    List<ErrorResult> getErrors();

    boolean hasErrors();

    T get();

    class Error<T> implements Result<T> {

        private final List<ErrorResult> errors = new ArrayList<>();

        private Error(final String code, final String message) {
            errors.add(new ErrorResult(code, message));
        }

        private Error(final List<ErrorResult> errs) {
            errors.addAll(errs);
        }


        @Override
        public List<ErrorResult> getErrors() {
            return errors;
        }

        @Override
        public boolean hasErrors() {
            return true;
        }

        @Override
        public T get() {
            throw new NoSuchElementException("Error result does not have an underlying object");
        }
    }

    class Ok<T> implements Result<T> {

        private final T result;

        private Ok(final T result) {
            this.result = result;
        }

        @Override
        public List<ErrorResult> getErrors() {
            throw new NoSuchElementException("OK result does not have any errors");
        }

        @Override
        public boolean hasErrors() {
            return false;
        }

        @Override
        public T get() {
            return result;
        }
    }
}
