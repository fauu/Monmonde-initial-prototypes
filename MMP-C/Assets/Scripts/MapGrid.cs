using UnityEngine;
using System.Collections.Generic;

[RequireComponent(typeof(MeshFilter), typeof(MeshRenderer))]
public class MapGrid : MonoBehaviour {
	private int xSize = 2, ySize = 2;
	private Vector3[] vertices;
	private Mesh mesh;

	private void Awake() 
	{
		Generate();
	}

    private void Generate()
    {
		int tileCount = xSize * ySize;

		Material[] materials = new Material[tileCount];
		string[] textureNames = new string[] {"82", "83", "67", "68"};

		for (int i = 0; i < tileCount; i++)
		{
			Material material = new Material(Shader.Find("Unlit/Texture"));
			material.mainTexture = Resources.Load("WorldMap/world_" + textureNames[i]) as Texture2D;
			materials[i] = material;
		}
		GetComponent<Renderer>().materials = materials;

		GetComponent<MeshFilter>().mesh = mesh = new Mesh();

		vertices = new Vector3[tileCount * 4];
		Vector2[] uv = new Vector2[vertices.Length];
		for (int i = 0, y = 0; y < ySize; y++) 
		{
			for (int x = 0; x < xSize; x++, i += 4)
			{
				vertices[i] = new Vector3(x, y);
				uv[i] = new Vector2(0, 0);
				vertices[i + 1] = new Vector3(x, y + 1);
				uv[i + 1] = new Vector2(0, 1);
				vertices[i + 2] = new Vector3(x + 1, y);
				uv[i + 2] = new Vector2(1, 0);
				vertices[i + 3] = new Vector3(x + 1, y + 1);
				uv[i + 3] = new Vector2(1, 1);
			}
		}
		mesh.vertices = vertices;
		mesh.uv = uv;

		mesh.subMeshCount = tileCount;

		for (int smi = 0, vi = 0, y = 0; y < ySize; y++)
		{
			for (int x = 0; x < xSize; x++, vi += 4)
			{
				int[] subMeshTriangles = new int[6];
				subMeshTriangles[0] = vi;
				subMeshTriangles[1] = vi + 1;
				subMeshTriangles[2] = vi + 2;
				subMeshTriangles[3] = subMeshTriangles[2];
				subMeshTriangles[4] = subMeshTriangles[1];
				subMeshTriangles[5] = vi + 3;
				mesh.SetTriangles(subMeshTriangles, smi++);
			}
		}
    }

	private void OnDrawGizmos()
	{
		if (vertices == null)
		{
			return;
		}

		Gizmos.color = Color.black;

		for (int i = 0; i < vertices.Length; i++)
		{
			Gizmos.DrawSphere(vertices[i], 100.1f);
		}
	}
}