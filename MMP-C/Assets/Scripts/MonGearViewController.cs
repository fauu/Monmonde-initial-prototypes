 using UnityEngine;
 using UnityEngine.EventSystems;
 
 namespace Monmonde {
	public class MonGearViewController : MonoBehaviour {
		private bool hasFocus = false;
		private CanvasGroup cv;
		private bool scheduleShow = false;
	
		public void Start()
		{
			GameObject monGear = transform.Find("MonGear").gameObject;
			monGear.AddComponent<EventTrigger>();

			EventTrigger eventTrigger = monGear.GetComponent<EventTrigger>();

			EventTrigger.TriggerEvent enterTriggerEvent = new EventTrigger.TriggerEvent();
			enterTriggerEvent.AddListener((data) => OnPointerEnter());
			EventTrigger.Entry enterEntry = new EventTrigger.Entry() {
				callback = enterTriggerEvent, 
				eventID = EventTriggerType.PointerEnter
			};
			eventTrigger.triggers.Add(enterEntry);

			EventTrigger.TriggerEvent exitTriggerEvent = new EventTrigger.TriggerEvent();
			exitTriggerEvent.AddListener((data) => OnPointerExit());
			EventTrigger.Entry exitEntry = new EventTrigger.Entry() {
				callback = exitTriggerEvent, 
				eventID = EventTriggerType.PointerExit
			};
			eventTrigger.triggers.Add(exitEntry);

			cv = monGear.GetComponent<CanvasGroup>();
		}
	
		public void Update()
		{
			if (!scheduleShow && !hasFocus) 
			{
				if (Input.GetMouseButtonUp(0))
				{
					cv.alpha = 0;
					cv.blocksRaycasts = false;
					gameObject.SetActive(false);	
				}
	
			} 
			else if (scheduleShow) 
			{
				cv.alpha = 1;
				cv.blocksRaycasts = true;
				scheduleShow = false;
			}
		}
	
		public void Show()
		{
			scheduleShow = true;            
		}
	
		public void OnPointerExit()
		{
			hasFocus = false;        
		}
	
		public void OnPointerEnter()
		{
			hasFocus = true;               
		}
	}
 }