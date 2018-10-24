package id.web.himawari.processor;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import id.web.himawari.models.MusicMetadata;

/**
 * 
 * @author joseph.tarigan@gmail.com
 *
 */
public interface AudioFileProcessing {

	static final String UNKNOWN_ARTIST = "Unknown Artist";
	static final String UNKNOWN_ALBUM = "Unknown Album";
	
	public abstract MusicMetadata getMetadata (File file, File folder) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException;
}