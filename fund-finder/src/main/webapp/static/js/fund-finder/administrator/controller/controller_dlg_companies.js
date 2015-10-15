angular.module('fundFinder')

.controller('Administrator_CompaniesCtrl', function($rootScope, $scope, $state, $element, close, type, label, data) {
	if (type == 'COMPANIES_BY_SECTOR') {
		$scope.title = "Tvrtke koje pripadaju sektoru '" + label + "'";
	} else if (type == 'COMPANIES_BY_LOCATION') {
		$scope.title = "Tvrtke koje se nalaze u županiji '" + label + "'";
	} else if (type == 'INVESTMENTS') {
		$scope.title = "Tvrtke koje su odabrale investiciju '" + label + "'";
	} else if (type == 'REVENUES') {
		$scope.title = "Tvrtke koje imaju prihode '" + label + "' kuna";
	}
	
	$scope.gridOptions = {
		enableScrollbars: false,
		paginationPageSizes: [10, 20, 30, 50, 100],
		paginationPageSize: 10,
		useExternalPagination: false,
		enableFiltering: false,
		useExternalFiltering: false,
		totalItems: 10,
		rowHeight: 32,
		enableHorizontalScrollbar: 0,
		enableVerticalScrollbar: 0,
		enableColumnMenus: false,
		enableGridMenu: false,
		columnDefs: [
			{
				name: 'Tvrtka',
				field: 'company.name',
				type: 'string',
				cellTooltip: false, 
				enableSorting: true,
				enableFiltering: true,
				enableHiding: false
			},
			{
				displayName: 'OIB',
				field: 'company.oib',
				type: 'string',
				cellTooltip: false, 
				enableSorting: true,
				enableFiltering: true,
				enableHiding: false,
				width: 150
			},
			{
				name: 'Ime',
				field: 'firstName',
				type: 'string',
				cellTooltip: false, 
				enableSorting: true,
				enableFiltering: true,
				enableHiding: false
			},
			{
				name: 'Prezime',
				field: 'lastName',
				type: 'string',
				cellTooltip: false, 
				enableSorting: true,
				enableFiltering: true,
				enableHiding: false
			},
			{
				name: 'Akcija',
				type: 'string',
				cellTooltip: false, 
				enableSorting: false,
				enableFiltering: false,
				enableHiding: false,
				width: 60,
				cellTemplate:'<div style="padding-top: 1px"><button ng-click="grid.appScope.showUser(row.entity)" class="btn-xs btn-white m-l-xs"><i class="fa fa-2x fa-eye"></i></button></div>'
			}
		]
	};
	
	$scope.gridOptions.data = data;
	$scope.gridOptions.totalItems = data.length;
	$scope.gridOptions.onRegisterApi = function (gridApi) {
		var newHeight = data.length * 32 + (($scope.gridOptions.totalItems == 0) ? 62 : 62);
		angular.element(document.getElementsByClassName('grid')[0]).css('height', newHeight + 'px');
	};
	
	$scope.closeModal = function(result) {
		$element.modal('hide');
		close(result, 500);
	};
	
	$scope.showUser = function(entity) {
		$scope.closeModal('ok');
		$state.go('administrator.user_show', { 'id' : entity.id });
	};
	
})