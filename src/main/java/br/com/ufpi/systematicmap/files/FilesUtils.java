package br.com.ufpi.systematicmap.files;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.ufpi.systematicmap.model.MapStudy;

@Dependent
public class FilesUtils {
	
	private final Path DEFAULT_FOLDER = Paths.get("/tmp/uploads/");
	
	@PostConstruct
	public void init() {
		if(!Files.exists(DEFAULT_FOLDER)) {
			try {
				Files.createDirectories(DEFAULT_FOLDER);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public boolean save(UploadedFile file, MapStudy map) throws IOException {
		if(file != null) {
    		Path path = DEFAULT_FOLDER.resolve(map.getId().toString());
    		InputStream is = file.getFile();
//    		System.out.println(is.);
    		//TODO resolver problema do caractere especial abaixo
//    		String temp = is.toString().replaceAll("’", "'");
//    		InputStream stream = new ByteArrayInputStream(temp.getBytes(StandardCharsets.UTF_8));
//    		InputStream in = IOUtils.toInputStream(source, "UTF-8");
    		
    		try {
    			Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
    		    return true;
    		} catch (IOException e) {
//    		    throw new IllegalStateException(e);
    			return false;
    		}
		}
		return false;
	}

	protected String getFileName(MapStudy map) {
		return "Articles_" + map.getId().toString() + ".bib";
	}
	
	public File getFile(MapStudy map) {
		return DEFAULT_FOLDER.resolve(map.getId().toString()).toFile();
	}
}
