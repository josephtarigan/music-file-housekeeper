package id.web.himawari.processor.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.mp4.Mp4FieldKey;
import org.jaudiotagger.tag.mp4.Mp4Tag;

import id.web.himawari.models.MusicMetadata;
import id.web.himawari.processor.AudioFileProcessing;
import id.web.himawari.util.Strings;

/**
 * 
 * @author joseph.tarigan@gmail.com
 *
 */
public class Mp4Processor implements AudioFileProcessing {

	public MusicMetadata getMetadata(File file, File folder)
			throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		AudioFile mp4File = AudioFileIO.read(file);
		MusicMetadata musicMetadata= new MusicMetadata();
		try {
			Mp4Tag mp4Tag = (Mp4Tag) mp4File.getTag();
			String artist = Strings.stripIllegalCharacters(Strings.isNullOrEmpty(mp4Tag.getFirst(Mp4FieldKey.ALBUM_ARTIST)) ? mp4Tag.getFirst(Mp4FieldKey.ARTIST): mp4Tag.getFirst(Mp4FieldKey.ALBUM_ARTIST));
			String album = Strings.stripIllegalCharacters(mp4Tag.getFirst(Mp4FieldKey.ALBUM));
			musicMetadata.setArtist(Strings.isNullOrEmpty(artist) ? UNKNOWN_ARTIST : artist.trim());
			musicMetadata.setAlbumName(Strings.isNullOrEmpty(album) ? UNKNOWN_ALBUM : album.trim());
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