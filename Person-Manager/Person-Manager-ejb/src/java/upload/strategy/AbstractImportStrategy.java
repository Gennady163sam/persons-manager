package upload.strategy;

import java.io.File;

public interface AbstractImportStrategy {
    public void importData(File file)  throws DuplicatePersonException;
}
