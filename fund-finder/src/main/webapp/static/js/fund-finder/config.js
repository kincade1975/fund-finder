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
            templateUrl: "views/administrator/dashboard/dashboard.html",
            controller: 'DashboardCtrl'
        })
        .state('fund_finder.user_overview', {
            url: "/user/overview",
            templateUrl: "views/administrator/user/overview.html",
            controller: 'Administrator_UserOverviewCtrl'
        })
        .state('fund_finder.tender_overview', {
            url: "/tender/overview",
            templateUrl: "views/administrator/tender/overview.html",
            controller: 'TenderOverviewCtrl'
        })
        .state('fund_finder.investment_overview', {
            url: "/investment/overview",
            templateUrl: "views/administrator/investment/overview.html",
            controller: 'Administrator_InvestmentOverviewCtrl'
        })
        .state('fund_finder.investment_edit', {
            url: "/investment/:mode/:id",
            templateUrl: "views/administrator/investment/edit.html",
            controller: 'Administrator_InvestmentEditCtrl',
            params: { 'id' : null, 'mode' : null }
        })
        .state('fund_finder.article_overview', {
            url: "/article/overview",
            templateUrl: "views/administrator/article/overview.html",
            controller: 'Administrator_ArticleOverviewCtrl'
        })
        .state('fund_finder.article_edit', {
            url: "/article/:mode/:id",
            templateUrl: "views/administrator/article/edit.html",
            controller: 'Administrator_ArticleEditCtrl',
            params: { 'id' : null, 'mode' : null }
        })
        .state('fund_finder.statistics', {
            url: "/statistics",
            templateUrl: "views/administrator/statistics/statistics.html",
            controller: 'StatisticsOverviewCtrl'
        })
}

angular
    .module('fundFinder')
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
		
		// toastr default settings
		toastr.options.progressBar = true;
		toastr.options.preventDuplicates = true;
		toastr.options.showMethod = 'slideDown'; 
		toastr.options.hideMethod = 'slideUp';
		
		// keep track of previous and current state via root scope variables
		$rootScope.previousState;
		$rootScope.currentState;
		$rootScope.$on('$stateChangeSuccess', function(ev, to, toParams, from, fromParams) {
		    $rootScope.previousState = from.name;
		    $rootScope.previousStateParams = fromParams;
		    $rootScope.currentState = to.name;
		    $rootScope.currentStateParams = toParams;
		});
		
    });
