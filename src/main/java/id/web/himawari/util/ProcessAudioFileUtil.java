package id.web.himawari.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import id.web.himawari.models.MusicMetadata;
import id.web.himawari.processor.AudioFileProcessing;

public class ProcessAudioFileUtil {

	private MainPaneLog logPane;
	private AudioFileProcessing fileProcessor;
	
	public ProcessAudioFileUtil (MainPaneLog logPane) {
		this.logPane = logPane;
	}
	
	public void setFileProcessor (AudioFileProcessing fileProcessor) {
		this.fileProcessor = fileProcessor;
	}
	
	public void process (File file, File folder) {
		if (fileProcessor == null) {
			logPane.log("File processor hasn't been set.");
			return;
		}
		
		try {
			MusicMetadata musicMetadata = fileProcessor.getMetadata(file, folder);
			createFolder(musicMetadata);
			moveFile(file.getName(), Paths.get(file.getAbsolutePath()), musicMetadata.getAlbumPath());
		} catch (FileAlreadyExistsException e) {
			logPane.log("- " + file.getName() + " is already exist.");
		} catch (CannotReadException e) {
			logPane.log("- Error while reading " + file.getName());
		} catch (IOException e) {
			logPane.log("- Error while reading " + file.getName());
		} catch (TagException e) {
			logPane.log("- Error while reading " + file.getName() + " metadata.");
		} catch (ReadOnlyFileException e) {
			logPane.log("- " + file.getName() + " cannot be read.");
		} catch (InvalidAudioFrameException e) {
			logPane.log("- " + file.getName() + " format is not valid.");
		}
	}

	private void createFolder (MusicMetadata musicMetadata) throws IOException {
		if (Files.notExists(musicMetadata.getArtistPath(), LinkOption.NOFOLLOW_LINKS)) {
			Files.createDirectory(musicMetadata.getArtistPath());
		}
		
		if (Files.notExists(musicMetadata.getAlbumPath(), LinkOption.NOFOLLOW_LINKS)) {
			Files.createDirectory(musicMetadata.getAlbumPath());
			logPane.log("+ Creating directory " + musicMetadata.getAlbumPath().toString());
		}
	}
	
	private void moveFile (String fileName, Path origin, Path destination) throws IOException {
		Files.move(origin, Paths.get(destination + File.separator + fileName));
		logPane.log("+ Moving " + origin + " to " + Paths.get(destination + File.separator + fileName));
	}
}