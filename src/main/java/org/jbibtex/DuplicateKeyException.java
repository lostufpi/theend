package org.jbibtex;

public class DuplicateKeyException extends IllegalArgumentException {
    public DuplicateKeyException(Key key){
        super(key.getValue());
    }
}
