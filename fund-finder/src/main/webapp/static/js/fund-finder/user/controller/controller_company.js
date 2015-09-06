angular.module('fundFinder')

.controller('User_CompanyEditCtrl', function($rootScope, $scope, $state, $stateParams, $sce, $timeout, Administrator_ConfigurationService, Administrator_InvestmentService, User_CompanyService) {

	/** event handler for 'Back' button */
	$scope.back = function() {
		if ($rootScope.previousState) {
			$state.go($rootScope.previousState, $rootScope.previousStateParams);
		} else {
			window.history.back();
		}
	};
	
	/** event handler for 'Edit' button */
	$scope.edit = function() {
		$state.go('user.company_edit', { 'mode' : 'edit' });
	};
	
	/** event handler for 'Save' button */
	$scope.save = function() {
		User_CompanyService.setCompany($scope.company)
			.success(function(data, status) {
				$scope.company = data;
				toastr.success('Podaci su uspješno spremljeni');
			})
			.error(function(data, status) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom spremanja podataka');
				}
			});
	}
	
	/** get company */
	User_CompanyService.getCompany()
		.success(function(data, status) {
			$scope.company = data;
			if ($scope.company.incomplete) {
				$state.go('user.company_edit', { 'mode' : 'edit' });
				$timeout(function() { 
					toastr.warning('Profil vaše tvrtke je nepotpun.<br>Molimo popunite sva obavezna polja.'); 
				}, 1000);
			}
			$scope.mode = $stateParams.mode;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
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