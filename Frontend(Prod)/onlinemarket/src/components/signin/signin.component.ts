import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { RecaptchaModule } from 'ng-recaptcha-2';
import { RouterModule, Router } from '@angular/router';
import { ISigninResponse, IUserIdResponse } from '../../model/class/interface/Products';
import { CookieServiceService } from '../../services/cookie-service.service';
import { UserService } from '../../services/user.service';
import { Observable } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RecaptchaModule, RouterModule],
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css'],
  providers : [CookieServiceService,UserService]
})
export class SigninComponent implements OnInit {
  signInForm!: FormGroup;
  signInResponse: ISigninResponse[] = [];
  userEmailId: string = '';
  @Output() loginSuccess = new EventEmitter<void>();

   // New properties for the popup
   showPopup: boolean = false;
   popupTitle: string = '';
   popupMessage: string = '';
   verifyError:boolean=false;

  constructor(private fb: FormBuilder, private userService: UserService, private authService: AuthService, private cookieService: CookieServiceService, private router: Router) {}

  ngOnInit() {
    this.signInForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      captchaResponse: ['']
    });
  }

  onCaptchaResolved(captchaResponse: string | null) {
    this.signInForm.patchValue({ captchaResponse });
    console.log('Captcha Response:', captchaResponse);
  }

  signIn() {
    if (this.signInForm.invalid) {
      //alert("Please fill in all required fields.");
      return;
    }

    const { email, password } = this.signInForm.value;

    if (!this.signInForm.value.captchaResponse) {
      //alert("Please verify that you are not a robot.");
      this.verifyError=true;
      this.popupTitle="Error";
      this.popupMessage="Please verify that you are not a robot.";
      return;
    }

    console.log("Submitting login request...");
    console.log("Email:", email);
    console.log("Password:", password);



    this.authService.signIn(email, password).then((result: any) => {
      console.log("User signed in:", result);
      if (result.isActive==0) {
        this.popupTitle = "Error";
        this.popupMessage = "Your account is inactive. Please contact support.";
        this.showPopup = true;
        return;
      }
      //alert("Login successful!");

      this.userEmailId = email;
      this.loginSuccess.emit(); // Emit event on successful login
      this.userService.handleLoginSuccess(this.userEmailId);

      const encoded = btoa(`${email}:${password}`);

      localStorage.setItem('authToken',encoded);
      console.log("Encoded = ", encoded);
      // const token = localStorage.getItem('authToken');

      // const headers = new HttpHeaders({
      //   Authorization: `Basic ${token}`
      // });



      this.router.navigate(['/home'])
    }).catch((err: { message: string; }) => {
      console.error('Login failed: ', err);
        this.popupTitle = 'Error';
        this.popupMessage = 'Failed to sign in. Try again.';
        this.showPopup = true;
      //alert("Error: "+ err.message);
    });
  }

  closePopup() {
    this.showPopup = false;
  }

  closeVerifyPopup(){
    this.verifyError=false;
  }
}
