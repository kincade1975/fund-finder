angular.module('fundFinder')

// ==============================================================================================================
// 	Forgot Password Controller
// ==============================================================================================================
.controller('ForgotPasswordCtrl', function($rootScope, $scope, $state, Administrator_PasswordService) {   
	$scope.email = null;
	
	$scope.forgotPassword = function() {
		Administrator_PasswordService.validateEmail($scope.email)
			.success(function(data, status) {
				if (data == true) {
					Administrator_PasswordService.forgotPassword($scope.email)
						.success(function(data, status) {
							toastr.success('Poslan vam je e-mail koji sadrži link do stranice gdje možete resetirati zaporku');
							$state.go('login');
						})
						.error(function(data, status) {
							toastr.error('Došlo je do pogreške prilikom procesiranja');
						});
				} else {
					toastr.warning('Korisnik sa unešenom e-mail adresom nije pronađen u sustavu');	
				}
			})
			.error(function(data, status) {
				toastr.error('Došlo je do pogreške prilikom procesiranja');
			});
	};
})

// ==============================================================================================================
// 	Reset Password Controller
// ==============================================================================================================
.controller('ResetPasswordCtrl', function($rootScope, $scope, $state, $stateParams, Administrator_PasswordService) {   
	$scope.password = "";
	$scope.confirmPassword = "";
	
	Administrator_PasswordService.validateUuid($stateParams.uuid)
		.success(function(data, status) {
			if (data == false) {
				toastr.warning('Invalidan UUID<br>Resetiranje zaporke nije moguće');
				$state.go('login');
			}
		})
		.error(function(data, status) {
			toastr.error('Došlo je do pogreške prilikom procesiranja');
		});
	
	$scope.resetPassword = function() {
		var data = {
    		"uuid" : $stateParams.uuid,
    		"password" : $scope.password
    	};
    	Administrator_PasswordService.resetPassword(data)
			.success(function(data, status) {
				toastr.success('Zaporka je uspješno resetirana');
				$state.go('login');
			})
			.error(function(data, status) {
				toastr.error('Došlo je do pogreške prilikom resetiranja zaporke');
			});
	};
	
})