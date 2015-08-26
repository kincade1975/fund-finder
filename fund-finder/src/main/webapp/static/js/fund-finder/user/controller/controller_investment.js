angular.module('fundFinder')

// ==============================================================================================================
// 	OVERVIEW
// ==============================================================================================================
.controller('User_InvestmentOverviewCtrl', function($rootScope, $scope, $state, User_InvestmentService) {
	
	User_InvestmentService.getInvestments().
		success(function(data, status, headers, config) {
			$scope.investments = data;
		}).
		error(function(data, status, headers, config) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	$scope.save = function() {
		User_InvestmentService.setInvestments($scope.investments)
			.success(function(data, status) {
				toastr.success('Podaci su uspješno spremljeni');
			})
			.error(function(data, status) {
				if (status == 403) {
					$state.go('login');
				} else {
					toastr.error('Došlo je do pogreške prilikom spremanja podatama');
				}
			});
	}
		
});