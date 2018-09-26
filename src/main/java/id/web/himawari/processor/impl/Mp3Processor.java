package id.web.himawari.processor.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;

import id.web.himawari.models.MusicMetadata;
import id.web.himawari.processor.AudioFileProcessing;
import id.web.himawari.util.Strings;

/**
 * 
 * @author joseph.tarigan@gmail.com
 *
 */
public class Mp3Processor implements AudioFileProcessing {

	public MusicMetadata getMetadata(File file, File folder) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		MP3File mp3File = (MP3File) AudioFileIO.read(file);
		MusicMetadata musicMetadata= new MusicMetadata();		
		try {
			AbstractID3v2Tag v2Tag = mp3File.getID3v2Tag();
			musicMetadata.setArtist(Strings.isNullOrEmpty(v2Tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST)) ? UNKNOWN_ARTIST : v2Tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST).trim());
			musicMetadata.setAlbumName(Strings.isNullOrEmpty(v2Tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM)) ? UNKNOWN_ALBUM : v2Tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM).trim());
		} catch (Exception e) {
			musicMetadata.setArtist(UNKNOWN_ARTIST);
			musicMetadata.setAlbumName(UNKNOWN_ALBUM);
		} finally {
			musicMetadata.setArtistPath(Paths.get(folder.getAbsolutePath() + File.separator + musicMetadata.getArtist()));		
			musicMetadata.setAlbumPath(Paths.get(musicMetadata.getArtistPath().toString() + File.separator + musicMetadata.getAlbumName()));			
		}
	
		return musicMetadata;
	}
}