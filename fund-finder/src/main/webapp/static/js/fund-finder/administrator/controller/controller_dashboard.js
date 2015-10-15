angular.module('fundFinder')

.controller('Administrator_DashboardCtrl', function($rootScope, $scope, $state, ModalService, Administrator_DashboardService, Administrator_DlgCompaniesService) {   

	Administrator_DashboardService.getLatestUsers()
		.success(function(data, status) {
			$scope.users = data;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja najnovijih korisnika');
			}
		});
	
	Administrator_DashboardService.getLatestTenders()
		.success(function(data, status) {
			$scope.tenders = data;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja najnovijih natječaja');
			}
		});
	
	Administrator_DashboardService.getStatistics()
		.success(function(data, status) {
			// COMPANIES_BY_SECTOR
			$scope.type0 = data[0].type;
			$scope.labels0 = data[0].labels;
			$scope.data0 = data[0].data;
			
			// COMPANIES_BY_LOCATION
			$scope.type1 = data[1].type;
			$scope.labels1 = data[1].labels;
			$scope.data1 = data[1].data;
			
			// TOP_INVESTMENTS
			$scope.type2 = data[2].type;
			$scope.labels2 = data[2].labels;
			$scope.data2 = data[2].data;
			
			// TOP_REVENUES
			$scope.type3 = data[3].type;
			$scope.labels3 = data[3].labels;
			$scope.data3 = data[3].data;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
			}
		});
	
	$scope.showUser = function(user) {
		$state.go('administrator.user_show', { 'id' : user.id });
	};
	
	$scope.showTender = function(tender) {
		$state.go('administrator.tender_edit', { 'id' : tender.id, 'mode' : 'show' });
	};
	
	$scope.showStatistics = function(type) {
		$state.go('administrator.statistics', { 'type' : type });
	}
	
	$scope.showCompanies = function(obj, event) {
		if (obj && obj[0]) {
			Administrator_DlgCompaniesService.getCompanies(event.srcElement.id, obj[0].label)
				.success(function(data, status) {
					ModalService.showModal({
						templateUrl: "dialogs/companies.html",
						controller: "Administrator_CompaniesCtrl",
						inputs: {
							type: event.srcElement.id,
							label: obj[0].label,
							data: data
						}
					}).then(function(modal) {
						modal.element.modal();
					});
				})
				.error(function(data, status) {
					if (status == 403) {
						$state.go('login');
					} else {
						toastr.error('Došlo je do pogreške prilikom dohvaćanja podataka');
					}
				});
		}
	}
	
})
