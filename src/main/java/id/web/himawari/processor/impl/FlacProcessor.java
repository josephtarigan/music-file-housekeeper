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
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentFieldKey;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

import id.web.himawari.models.MusicMetadata;
import id.web.himawari.processor.AudioFileProcessing;
import id.web.himawari.util.Strings;

/**
 * 
 * @author joseph.tarigan@gmail.com
 *
 */
public class FlacProcessor implements AudioFileProcessing {

	public MusicMetadata getMetadata(File file, File folder)
			throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		AudioFile flacFile = AudioFileIO.read(file);
		MusicMetadata musicMetadata = new MusicMetadata();
		
		try {
			FlacTag flacTag = (FlacTag) flacFile.getTag();
			VorbisCommentTag vorbisTag = flacTag.getVorbisCommentTag();
			String artist = Strings.stripIllegalCharacters(Strings.isNullOrEmpty(vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMARTIST)) ? vorbisTag.getFirst(VorbisCommentFieldKey.ARTIST) : vorbisTag.getFirst(VorbisCommentFieldKey.ALBUMARTIST));
			String album = Strings.stripIllegalCharacters(vorbisTag.getFirst(VorbisCommentFieldKey.ALBUM));
			musicMetadata.setArtist(Strings.isNullOrEmpty(artist) ? UNKNOWN_ARTIST : artist);
			musicMetadata.setAlbumName(Strings.isNullOrEmpty(album) ? UNKNOWN_ALBUM : album);
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