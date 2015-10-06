function config($stateProvider, $urlRouterProvider, $ocLazyLoadProvider, IdleProvider) {
    
	IdleProvider.idle(300);
	IdleProvider.timeout(9);
	
	$urlRouterProvider.when('/', '/login').otherwise('/');

    $ocLazyLoadProvider.config({ debug: false });

    $stateProvider
    
    	// ===========================================================================================
    	// 	ADMINISTRATOR 
    	// ===========================================================================================
        .state('administrator', {
            abstract: true,
            url: "/fund_finder",
            templateUrl: "views/common/content.html"
        })
        .state('login', {
			url: '/login',
			templateUrl: 'views/common/login.html',
			controller: 'SecurityCtrl'
		})
		.state('registration', {
			url: '/registration',
			templateUrl: 'views/common/registration.html',
			controller: 'RegistrationCtrl',
			resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_registration.js',
            			        'js/fund-finder/administrator/service/service_registration.js']
            		});
            	}
            }
		})
		.state('forgot_password', {
			url: '/password/forgot',
			templateUrl: 'views/common/forgot_password.html',
			controller: 'ForgotPasswordCtrl',
			resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_password.js',
            			        'js/fund-finder/administrator/service/service_password.js']
            		});
            	}
            }
		})
		.state('reset_password', {
			url: '/password/reset?uuid',
			templateUrl: 'views/common/reset_password.html',
			controller: 'ResetPasswordCtrl',
			resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_password.js',
            			        'js/fund-finder/administrator/service/service_password.js']
            		});
            	}
            }
		})
        .state('administrator.dashboard', {
            url: "/administrator/dashboard",
            templateUrl: "views/administrator/dashboard/dashboard.html",
            controller: 'DashboardCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_dashboard.js',
            			        'js/fund-finder/administrator/service/service_dashboard.js']
            		});
            	}
            }
        })
        .state('administrator.user_overview', {
            url: "/administrator/user/overview",
            templateUrl: "views/administrator/user/overview.html",
            controller: 'Administrator_UserOverviewCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_user.js',
            			        'js/fund-finder/administrator/service/service_user.js']
            		});
            	}
            }
        })
        .state('administrator.user_show', {
            url: "/administrator/user/show/:id",
            templateUrl: "views/administrator/user/show.html",
            controller: 'Administrator_UserShowCtrl',
            params: { 'id' : null },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_user.js',
            			        'js/fund-finder/administrator/service/service_user.js',
            			        'js/fund-finder/administrator/service/service_investment.js']
            		});
            	}
            }
        })
        .state('administrator.user_edit', {
            url: "/administrator/user/edit/:id",
            templateUrl: "views/administrator/user/edit.html",
            controller: 'Administrator_UserEditCtrl',
            params: { 'id' : null },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_user.js',
            			        'js/fund-finder/administrator/service/service_user.js']
            		});
            	}
            }
        })
        .state('administrator.tender_overview', {
            url: "/administrator/tender/overview",
            templateUrl: "views/administrator/tender/overview.html",
            controller: 'Administrator_TenderOverviewCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_tender.js',
            			        'js/fund-finder/administrator/service/service_tender.js']
            		});
            	}
            }
        })
        .state('administrator.tender_edit', {
            url: "/administrator/tender/:mode/:id",
            templateUrl: "views/administrator/tender/edit.html",
            controller: 'Administrator_TenderEditCtrl',
            params: { 'id' : null, 'mode' : null },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_tender.js',
            			        'js/fund-finder/administrator/service/service_tender.js',
            			        'js/fund-finder/administrator/service/service_investment.js',
            			        'js/fund-finder/administrator/service/service_configuration.js']
            		});
            	}
            }
        })
        .state('administrator.investment_overview', {
            url: "/administrator/investment/overview",
            templateUrl: "views/administrator/investment/overview.html",
            controller: 'Administrator_InvestmentOverviewCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_investment.js',
            			        'js/fund-finder/administrator/service/service_investment.js']
            		});
            	}
            }
        })
        .state('administrator.investment_edit', {
            url: "/administrator/investment/:mode/:id",
            templateUrl: "views/administrator/investment/edit.html",
            controller: 'Administrator_InvestmentEditCtrl',
            params: { 'id' : null, 'mode' : null },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_investment.js',
            			        'js/fund-finder/administrator/service/service_investment.js']
            		});
            	}
            }
        })
        .state('administrator.article_overview', {
            url: "/administrator/article/overview",
            templateUrl: "views/administrator/article/overview.html",
            controller: 'Administrator_ArticleOverviewCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_article.js',
            			        'js/fund-finder/administrator/service/service_article.js']
            		});
            	}
            }
        })
        .state('administrator.article_edit', {
            url: "/administrator/article/:mode/:id",
            templateUrl: "views/administrator/article/edit.html",
            controller: 'Administrator_ArticleEditCtrl',
            params: { 'id' : null, 'mode' : null },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_article.js',
            			        'js/fund-finder/administrator/service/service_article.js']
            		});
            	}
            }
        })
        .state('administrator.statistics', {
            url: "/administrator/statistics",
            templateUrl: "views/administrator/statistics/statistics.html",
            controller: 'StatisticsOverviewCtrl'
        })
        .state('administrator.configuration_company', {
            url: "/administrator/configuration/company",
            templateUrl: "views/administrator/configuration/configuration.html",
            controller: 'Administrator_ConfigurationCtrl',
            data: { entityType : 'company' },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_configuration.js',
            			        'js/fund-finder/administrator/service/service_configuration.js',
            			        'js/fund-finder/administrator/service/service_investment.js']
            		});
            	}
            }
        })
        .state('administrator.configuration_tender', {
            url: "/administrator/configuration/tender",
            templateUrl: "views/administrator/configuration/configuration.html",
            controller: 'Administrator_ConfigurationCtrl',
            data: { entityType : 'tender' },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_configuration.js',
            			        'js/fund-finder/administrator/service/service_configuration.js',
            			        'js/fund-finder/administrator/service/service_investment.js']
            		});
            	}
            }
        })
        .state('administrator.configuration_preview', {
            url: "/administrator/configuration/:entityType/preview",
            templateUrl: "views/administrator/configuration/preview.html",
            controller: 'Administrator_ConfigurationCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_configuration.js',
            			        'js/fund-finder/administrator/service/service_configuration.js',
            			        'js/fund-finder/administrator/service/service_investment.js']
            		});
            	}
            }
        })
        
        // ===========================================================================================
        // 	USER 
        // ===========================================================================================
        .state('user', {
            abstract: true,
            url: "/fund_finder",
            templateUrl: "views/common/content.html",
        })
        .state('user.user_edit', {
            url: "/user/user/edit/:id",
            templateUrl: "views/administrator/user/edit.html",
            controller: 'Administrator_UserEditCtrl',
            params: { 'id' : null },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/administrator/controller/controller_user.js',
            			        'js/fund-finder/administrator/service/service_user.js']
            		});
            	}
            }
        })
        .state('user.company_edit', {
            url: "/user/company/:mode",
            templateUrl: "views/user/company/edit.html",
            controller: 'User_CompanyEditCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/user/controller/controller_company.js',
            			        'js/fund-finder/user/service/service_company.js',
            			        'js/fund-finder/administrator/service/service_investment.js',
            			        'js/fund-finder/administrator/service/service_configuration.js']
            		});
            	}
            }
        })
        .state('user.investment_overview', {
            url: "/user/investment/overview",
            templateUrl: "views/user/investment/overview.html",
            controller: 'User_InvestmentOverviewCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/user/controller/controller_investment.js',
            			        'js/fund-finder/user/service/service_investment.js']
            		});
            	}
            }
        })
        .state('user.opportunity_overview', {
            url: "/user/opportunity/overview",
            templateUrl: "views/user/opportunity/overview.html",
            controller: 'User_OpportunityOverviewCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/user/controller/controller_opportunity.js',
            			        'js/fund-finder/user/service/service_opportunity.js',
            			        'js/fund-finder/user/service/service_company.js',]
            		});
            	}
            }
        })
        .state('user.opportunity_show', {
            url: "/user/opportunity/:id",
            templateUrl: "views/user/opportunity/show.html",
            controller: 'User_OpportunityShowCtrl',
            params: { 'id' : null },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/user/controller/controller_opportunity.js',
            			        'js/fund-finder/user/service/service_opportunity.js',
            			        'js/fund-finder/administrator/service/service_tender.js',
            			        'js/fund-finder/administrator/service/service_investment.js',
            			        'js/fund-finder/administrator/service/service_configuration.js']
            		});
            	}
            }
        })
        .state('user.article_overview', {
            url: "/user/article/overview",
            templateUrl: "views/user/article/overview.html",
            controller: 'User_ArticleOverviewCtrl',
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/user/controller/controller_article.js',
            			        'js/fund-finder/user/service/service_article.js']
            		});
            	}
            }
        })
        .state('user.article_show', {
            url: "/user/article/:id",
            templateUrl: "views/user/article/show.html",
            controller: 'User_ArticleShowCtrl',
            params: { 'id' : null },
            resolve: {
            	loadPlugin: function ($ocLazyLoad) {
            		return $ocLazyLoad.load({
            			name: 'fundFinder',
            			files: ['js/fund-finder/user/controller/controller_article.js',
            			        'js/fund-finder/user/service/service_article.js']
            		});
            	}
            }
        })
}

angular
    .module('fundFinder')
    .config(config)
    .run(function($rootScope, $state, $modal, $http, Idle) {
    	
    	$rootScope.dateFormat = "dd.MM.yyyy";
    	$rootScope.dateTimeFormat = "dd.MM.yyyy HH:mm"
    	
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
