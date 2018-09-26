package id.web.himawari.models;

import java.nio.file.Path;

/**
 * 
 * @author joseph.tarigan@gmail.com
 *
 */
public class MusicMetadata {

	private String artist;
	private String albumName;
	private Path artistPath;			
	private Path albumPath;
	
	public Path getArtistPath() {
		return artistPath;
	}
	public void setArtistPath(Path artistPath) {
		this.artistPath = artistPath;
	}
	public Path getAlbumPath() {
		return albumPath;
	}
	public void setAlbumPath(Path albumPath) {
		this.albumPath = albumPath;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
}