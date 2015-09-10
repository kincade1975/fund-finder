angular.module('fundFinder')

// ==============================================================================================================
// 	Registration Controller
// ==============================================================================================================
.controller('RegistrationCtrl', function($rootScope, $scope, $state, Administrator_RegistrationService) {   

	Administrator_RegistrationService.getRegistrationResource()
		.success(function(data, status) {
			$scope.registration = data;
		})
		.error(function(data, status) {
			toastr.error('Došlo je do pogreške prilikom procesiranja');
		});
	
	$scope.register = function() {
		if ($scope.registration.password != $scope.registration.confirmPassword) {
			toastr.warning("Zaporke se ne podudaraju<br>Molimo pokušajte opet");
			return;
		}
		
		Administrator_RegistrationService.validateEmail($scope.registration.email)
			.success(function(data, status) {
				if (data == true) {
					Administrator_RegistrationService.register($scope.registration)
						.success(function(data, status) {
							$state.go('login');
						})
						.error(function(data, status) {
							toastr.error('Došlo je do pogreške prilikom registracije korisnika');
						});
				} else {
					toastr.warning("Korisnik sa upisanom email adresom je već registriran");
				}
			})
			.error(function(data, status) {
				toastr.error('Došlo je do pogreške prilikom validacije email-a');
			});
	};
	
})