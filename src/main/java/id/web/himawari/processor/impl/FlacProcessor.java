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
			musicMetadata.setArtist(Strings.isNullOrEmpty(vorbisTag.getFirst(VorbisCommentFieldKey.ARTIST)) ? UNKNOWN_ARTIST : vorbisTag.getFirst(vorbisTag.getFirst(VorbisCommentFieldKey.ARTIST)).trim());
			musicMetadata.setAlbumName(Strings.isNullOrEmpty(vorbisTag.getFirst(VorbisCommentFieldKey.ALBUM)) ? UNKNOWN_ALBUM : vorbisTag.getFirst(vorbisTag.getFirst(VorbisCommentFieldKey.ALBUM)).trim());
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