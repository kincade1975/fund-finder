function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider) {
    
	IdleProvider.idle(300);
	IdleProvider.timeout(9);
	
	$urlRouterProvider.when('/', '/login').otherwise('/');

    $ocLazyLoadProvider.config({ debug: false });

    $stateProvider
        .state('administrator', {
            abstract: true,
            url: "/fund_finder",
            templateUrl: "views/common/content.html",
        })
        .state('login', {
			url: '/login',
			templateUrl: 'views/common/login.html',
			controller: 'SecurityCtrl'
		})
        .state('administrator.dashboard', {
            url: "/administrator/dashboard",
            templateUrl: "views/administrator/dashboard/dashboard.html",
            controller: 'DashboardCtrl'
        })
        .state('administrator.user_overview', {
            url: "/administrator/user/overview",
            templateUrl: "views/administrator/user/overview.html",
            controller: 'Administrator_UserOverviewCtrl'
        })
        .state('administrator.tender_overview', {
            url: "/administrator/tender/overview",
            templateUrl: "views/administrator/tender/overview.html",
            controller: 'TenderOverviewCtrl'
        })
        .state('administrator.investment_overview', {
            url: "/administrator/investment/overview",
            templateUrl: "views/administrator/investment/overview.html",
            controller: 'Administrator_InvestmentOverviewCtrl'
        })
        .state('administrator.investment_edit', {
            url: "/administrator/investment/:mode/:id",
            templateUrl: "views/administrator/investment/edit.html",
            controller: 'Administrator_InvestmentEditCtrl',
            params: { 'id' : null, 'mode' : null }
        })
        .state('administrator.article_overview', {
            url: "/administrator/article/overview",
            templateUrl: "views/administrator/article/overview.html",
            controller: 'Administrator_ArticleOverviewCtrl'
        })
        .state('administrator.article_edit', {
            url: "/administrator/article/:mode/:id",
            templateUrl: "views/administrator/article/edit.html",
            controller: 'Administrator_ArticleEditCtrl',
            params: { 'id' : null, 'mode' : null }
        })
        .state('administrator.statistics', {
            url: "/administrator/statistics",
            templateUrl: "views/administrator/statistics/statistics.html",
            controller: 'StatisticsOverviewCtrl'
        })
        .state('administrator.configuration_company', {
            url: "/administrator/configuration/company",
            templateUrl: "views/administrator/configuration/company.html",
            controller: 'Administrator_ConfigurationCompanyCtrl'
        })
        .state('administrator.configuration_company_preview', {
            url: "/administrator/configuration/company/preview",
            templateUrl: "views/administrator/configuration/company_preview.html",
            controller: 'Administrator_ConfigurationCompanyCtrl'
        })
        .state('administrator.configuration_tender', {
            url: "/administrator/configuration/tender",
            templateUrl: "views/administrator/configuration/tender.html",
            controller: 'Administrator_ConfigurationTenderCtrl'
        })
        
        .state('user', {
            abstract: true,
            url: "/fund_finder",
            templateUrl: "views/common/content.html",
        })
        .state('user.article_overview', {
            url: "/user/article/overview",
            templateUrl: "views/user/article/overview.html",
            controller: 'User_ArticleOverviewCtrl'
        })
        .state('user.article_show', {
            url: "/user/article/:id",
            templateUrl: "views/user/article/show.html",
            controller: 'User_ArticleShowCtrl',
            params: { 'id' : null }
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
