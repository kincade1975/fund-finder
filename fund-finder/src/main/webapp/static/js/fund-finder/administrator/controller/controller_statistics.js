angular.module('fundFinder')

.controller('Administrator_StatisticsCtrl', function($rootScope, $scope, $state, $stateParams, ModalService, Administrator_StatisticsService, Administrator_DlgCompaniesService) {
	
	var type = $stateParams.type;
	if (!type) {
		type = "COMPANIES_BY_SECTOR";
	}
	
	if (type == 'COMPANIES_BY_SECTOR') {
		$scope.active0 = true;
		$scope.active1 = false;
		$scope.active2 = false;
		$scope.active3 = false;
	} else if (type == 'COMPANIES_BY_LOCATION') {
		$scope.active0 = false;
		$scope.active1 = true;
		$scope.active2 = false;
		$scope.active3 = false;
	} else if (type == 'INVESTMENTS') {
		$scope.active0 = false;
		$scope.active1 = false;
		$scope.active2 = true;
		$scope.active3 = false;
	} else if (type == 'REVENUES') {
		$scope.active0 = false;
		$scope.active1 = false;
		$scope.active2 = false;
		$scope.active3 = true;
	}
	
	Administrator_StatisticsService.getCompaniesBySector()
		.success(function(data, status) {
			$scope.type0 = data.type;
			$scope.labels0 = data.labels;
			var dataArray = new Array();
			dataArray.push(data.data);
			$scope.data0 = dataArray;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja statistike');
			}
		});
	
	Administrator_StatisticsService.getCompaniesByLocation()
		.success(function(data, status) {
			$scope.type1 = data.type;
			$scope.labels1 = data.labels;
			var dataArray = new Array();
			dataArray.push(data.data);
			$scope.data1 = dataArray;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja statistike');
			}
		});
	
	Administrator_StatisticsService.getInvestments()
		.success(function(data, status) {
			$scope.type2 = data.type;
			$scope.labels2 = data.labels;
			var dataArray = new Array();
			dataArray.push(data.data);
			$scope.data2 = dataArray;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja statistike');
			}
		});
	
	Administrator_StatisticsService.getRevenues()
		.success(function(data, status) {
			$scope.type3 = data.type;
			$scope.labels3 = data.labels;
			var dataArray = new Array();
			dataArray.push(data.data);
			$scope.data3 = dataArray;
		})
		.error(function(data, status) {
			if (status == 403) {
				$state.go('login');
			} else {
				toastr.error('Došlo je do pogreške prilikom dohvaćanja statistike');
			}
		});
	
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