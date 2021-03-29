using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Zenject;
using Tacticsoft;

namespace Monmonde.MonGear
{
	public class RankingTableController : MonoBehaviour
	{
		public GameObject rowPrefab;

		[Inject] private LeagueManager leagueManager;
		[Inject] private Player player;

		private void Start()
		{
			List<MonGearWorldRankingTableRowViewmodel> viewmodel = leagueManager.ranking;

			Trainer trainer = player.trainer;
			var playerEntry = new MonGearWorldRankingTableRowViewmodel(trainer.leagueRank.ToString(), trainer.fullName, trainer.sex, trainer.country.code, trainer.leagueRating.ToString());
			viewmodel.Add(playerEntry);

			for (int i = 0; i < viewmodel.Count; i++)
			{
				var row = Instantiate(rowPrefab);
				row.transform.SetParent(transform);

				var rowController = row.GetComponent<MonGearWorldRankingTableRowController>();
				rowController.SetData(viewmodel[i]);

				if (i == viewmodel.Count - 1)
				{
					rowController.MarkAsPlayerRow();
				}
			}
		}
	}
}