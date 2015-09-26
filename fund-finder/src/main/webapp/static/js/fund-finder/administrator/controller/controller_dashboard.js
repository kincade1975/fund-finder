angular.module('fundFinder')

.controller('DashboardCtrl', function($rootScope, $scope, $state, Administrator_DashboardService, Administrator_TotalService) {   

	Administrator_DashboardService.getLatestUsers()
		.success(function(data, status) {
			$scope.users = data;
		})
		.error(function(data, status) {
			toastr.error('Došlo je do pogreške prilikom dohvaćanja najnovijih korisnika');
		});
	
	Administrator_DashboardService.getLatestTenders()
		.success(function(data, status) {
			$scope.tenders = data;
		})
		.error(function(data, status) {
			toastr.error('Došlo je do pogreške prilikom dohvaćanja najnovijih natječaja');
		});
	
	Administrator_TotalService.updateTotal();
	
	$scope.showUser = function(user) {
		$state.go('administrator.user_show', { 'id' : user.id });
	};
	
	$scope.showTender = function(tender) {
		$state.go('administrator.tender_edit', { 'id' : tender.id, 'mode' : 'show' });
	};
	
})