angular.module('fundFinder')

.controller('User_OpportunityOverviewCtrl', function($rootScope, $scope, $state, $stateParams, $timeout, $interval, User_CompanyService, User_OpportunityService) {
	
	/** get company */
	User_CompanyService.getCompany()
		.success(function(data, status) {
			$scope.company = data;
			if ($scope.company.incomplete) {
				$scope.counter = 9;
				$interval(function() { 
					$scope.counter = $scope.counter - 1; 
					if ($scope.counter == 0) {
						$timeout(function() {
							angular.element('#link2EditCompany').triggerHandler('click');
						}, 0);
					}
				}, 1000);
			} else {
				User_OpportunityService.findTenders()
					.success(function(data, status, headers, config) {
						$scope.tenders = data;
					})
					.error(function(data, status, headers, config) {
						if (status == 403) {
							$state.go('login');
						} else {
							toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
						}
					});
			}
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	$scope.showTender = function(entity) {
		$state.go('user.opportunity_show', { 'id' : entity.id });
	};
	
	$scope.editCompany = function() {
		$state.go('user.company_edit', { 'mode' : 'edit' });	
	};
	
})

.controller('User_OpportunityShowCtrl', function($rootScope, $scope, $state, $stateParams, Administrator_TenderService, Administrator_ConfigurationService, Administrator_InvestmentService) {
	
	$scope.back = function() {
		if ($rootScope.previousState) {
			$state.go($rootScope.previousState, $rootScope.previousStateParams);
		} else {
			window.history.back();
		}
	};
	
	Administrator_TenderService.getTender($stateParams.id)
		.success(function(data, status) {
			$scope.tender = data;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja natječaja');
			}
		});

	/** get cities */
	Administrator_ConfigurationService.getCities()
		.success(function(data, status, headers, config) {
			$scope.cities = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** get counties */
	Administrator_ConfigurationService.getCounties()
		.success(function(data, status, headers, config) {
			$scope.counties = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** get NKDs */
	Administrator_ConfigurationService.getNkds()
		.success(function(data, status, headers, config) {
			$scope.nkds = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** get investments */
	Administrator_InvestmentService.getInvestments()
		.success(function(data, status, headers, config) {
			$scope.investments = data;
		})
		.error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	/** text editor options */
	$scope.summernoteOptions = {
		height: 300,
		focus: false,
		airMode: false,
		toolbar: [['style', ['bold', 'italic', 'underline']], ['alignment', ['ul', 'ol', 'paragraph']], ['insert', ['link','hr']]]
	};
	
	$scope.toTrusted = function(html) {
	    return $sce.trustAsHtml(html);
	}

});