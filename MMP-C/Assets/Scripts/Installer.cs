using Zenject;

namespace Monmonde
{
    public class Installer : MonoInstaller
    {
        public override void InstallBindings()
        {
            Container.BindInterfacesAndSelfTo<EventManager>().AsSingle();
            Container.BindInterfacesAndSelfTo<ViewManager>().AsSingle();
            Container.BindInterfacesAndSelfTo<GeographyManager>().AsSingle();
            Container.BindInterfacesAndSelfTo<WorldTimeManager>().AsSingle();
            Container.BindInterfacesAndSelfTo<LeagueManager>().AsSingle();
            Container.BindInterfacesAndSelfTo<Player>().AsSingle();

            Container.Bind<GoToLocationViewButtonController>().FromComponentInHierarchy();
            Container.Bind<ClockUpdater>().FromComponentInHierarchy();
            Container.Bind<WorldMapLand>().FromComponentInHierarchy();
            Container.Bind<WorldMapLocationMarkers>().FromComponentInHierarchy();
            Container.Bind<MonGear.TheLeagueController>().FromComponentInHierarchy();
            Container.Bind<MonGear.PlayerProfileViewController>().FromComponentInHierarchy();
            Container.Bind<MonGear.RankingTableController>().FromComponentInHierarchy();
        }
    }
}