using UnityEngine;
using Zenject;
 
namespace Monmonde 
{
  public class WorldMapScroller : MonoBehaviour
  {
    public Camera targetCamera;

    [Inject] private EventManager eventManager;
    private Vector3 dragOrigin;
    private float moveSmooth = 0.1f;

    void Update()
    {
      if (Input.GetMouseButtonDown(0))
      {
        dragOrigin = new Vector3(Input.mousePosition.x, Input.mousePosition.y, 0);
        dragOrigin = targetCamera.ScreenToWorldPoint(dragOrigin);
      }

      if (Input.GetMouseButton(0))
      {
        Vector3 currentPos = new Vector3(Input.mousePosition.x, Input.mousePosition.y, 0);
        currentPos = targetCamera.ScreenToWorldPoint(currentPos);
        Vector3 movePos = dragOrigin - currentPos;
        targetCamera.transform.position += (movePos * moveSmooth);

        eventManager.QueueEvent(new WorldMapScrolledEvent(targetCamera.transform.position));
      }
    }
  }
}