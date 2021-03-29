using System.Collections.Generic;
using System;
using UnityEngine;
using Zenject;
using SQLite4Unity3d;

namespace Monmonde
{
	[RequireComponent(typeof(MeshFilter), typeof(MeshRenderer))]
	public class WorldMapLand : MonoBehaviour
	{
		public static Vector2 TileSpriteSize = new Vector2(256, 256);

		public SpriteRenderer tilePrefab;

		[Inject] private EventManager eventManager;
		private Vector2 originTileCoords = new Vector2(70, 85);
		private Vector2 centralTileCoords = new Vector2(70, 85);
		private string mapFilename = "WorldMap.mbtiles";
		private SQLiteConnection connection;
		private List<GameObject> tileObjects = new List<GameObject>();

		void Start() 
		{
			eventManager.AddListener<WorldMapScrolledEvent>(OnMapScrolled);

			string mapPath = string.Format(@"Assets/StreamingAssets/{0}", mapFilename);
			connection = new SQLiteConnection(mapPath, SQLiteOpenFlags.ReadOnly);

			UpdateMap();
		}

		void CreateTileSprite(Tile tile, Vector2 position)
		{
			var texture = new Texture2D((int) TileSpriteSize.x, (int) TileSpriteSize.y, TextureFormat.RGBA32, false);
			texture.LoadImage(tile.data);
			texture.filterMode = FilterMode.Point;

			SpriteRenderer tileSpriteRenderer = Instantiate(tilePrefab, new Vector3(position.x, position.y, 0), Quaternion.identity);
			Sprite tileSprite = Sprite.Create(texture, new Rect(0, 0, texture.width, texture.height), new Vector2(0.5f, 0.5f), 256, 0, SpriteMeshType.FullRect);
			tileSpriteRenderer.sprite = tileSprite;
			tileObjects.Add(tileSpriteRenderer.gameObject);
		}
		
		void OnMapScrolled(WorldMapScrolledEvent e)
		{
			Vector2 oldCentralTileCoords = centralTileCoords;

			centralTileCoords.x = (float) Math.Round((Decimal) originTileCoords.x + (Decimal) e.newPosition.x);
			centralTileCoords.y = (float) Math.Round((Decimal) originTileCoords.y + (Decimal) e.newPosition.y);

			if (centralTileCoords != oldCentralTileCoords)
			{
				UpdateMap();
			}
		}

		void UpdateMap()
		{
			tileObjects.ForEach(to => GameObject.Destroy(to));
			tileObjects.Clear();

			var columnRange = new Vector2(centralTileCoords.x - 4, centralTileCoords.x + 3);
			var rowRange = new Vector2(centralTileCoords.y - 3, centralTileCoords.y + 3);
			IEnumerable<Tile> tiles = connection.Table<Tile>().Where(t => t.zoomLevel == 7 && t.column >= columnRange.x && t.column <= columnRange.y && t.row >= rowRange.x && t.row <= rowRange.y);
			foreach (Tile tile in tiles)
			{
				var position = new Vector2(- originTileCoords.x + tile.column, - originTileCoords.y + tile.row);
				CreateTileSprite(tile, new Vector2(position.x, position.y));
			}

			Resources.UnloadUnusedAssets();
		}

		void Destroy()
		{
			connection.Close();
		}
	}
}