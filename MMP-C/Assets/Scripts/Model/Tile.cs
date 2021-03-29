using SQLite4Unity3d;

[Table("tiles")]
public class Tile {
	[Column("zoom_level")]
	public int zoomLevel { get; set; }

	[Column("tile_column")]
	public int column { get; set; }

	[Column("tile_row")]
	public int row { get; set; }

	[Column("tile_data")]
	public byte[] data { get; set; }
}
