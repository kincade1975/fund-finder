function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider) {
    
	IdleProvider.idle(300);
	IdleProvider.timeout(9);
	
	$urlRouterProvider.when('/', '/login').otherwise('/');

    $ocLazyLoadProvider.config({ debug: false });

    $stateProvider
        .state('fund_finder', {
            abstract: true,
            url: "/fund_finder",
            templateUrl: "views/common/content.html",
        })
        .state('login', {
			url: '/login',
			templateUrl: 'views/login.html',
			controller: 'SecurityCtrl'
		})
        .state('fund_finder.dashboard', {
            url: "/dashboard",
            templateUrl: "views/dashboard/dashboard.html",
            controller: 'DashboardCtrl'
        })
        .state('fund_finder.user_overview', {
            url: "/user/overview",
            templateUrl: "views/user/overview.html",
            controller: 'UserOverviewCtrl'
        })
        .state('fund_finder.tender_overview', {
            url: "/tender/overview",
            templateUrl: "views/tender/overview.html",
            controller: 'TenderOverviewCtrl'
        })
        .state('fund_finder.investment_overview', {
            url: "/investment/overview",
            templateUrl: "views/investment/overview.html",
            controller: 'InvestmentOverviewCtrl'
        })
        .state('fund_finder.article_overview', {
            url: "/article/overview",
            templateUrl: "views/article/overview.html",
            controller: 'ArticleOverviewCtrl'
        })
        .state('fund_finder.statistics', {
            url: "/statistics",
            templateUrl: "views/statistics/statistics.html",
            controller: 'StatisticsOverviewCtrl'
        })
}

angular
    .module('inspinia')
    .config(config)
    .run(function($rootScope, $state, $modal, $http, Idle) {
        $rootScope.$state = $state;
        
        $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
			if (toState.name == 'login') {
				Idle.unwatch();
			} else {
				Idle.watch();
			}
		});
        
        $rootScope.$on('IdleStart', function() {
			if ($rootScope.idleWarningDlg) {
				$rootScope.idleWarningDlg.close();
				$rootScope.idleWarningDlg = null;
			}

			$rootScope.idleWarningDlg = $modal.open({
				templateUrl: 'idle-warning-dialog.html',
				windowClass: 'modal-danger'
			});
		});

		$rootScope.$on('IdleEnd', function() {
			if ($rootScope.idleWarningDlg) {
				$rootScope.idleWarningDlg.close();
				$rootScope.idleWarningDlg = null;
			}
		});

		$rootScope.$on('IdleTimeout', function() {
			if ($rootScope.idleWarningDlg) {
				$rootScope.idleWarningDlg.close();
				$rootScope.idleWarningDlg = null;
			}
			
			$http.post('logout', {}).success(function() {
				$rootScope.authenticated = false;
				$state.go('login');
			}).error(function(data) {
				$rootScope.authenticated = false;
				$state.go('login');
			});
		});
    });
