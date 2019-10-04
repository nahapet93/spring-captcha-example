import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthenticationService } from './auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;
  password: string;
  captchaText: string;
  captchaSrc: string;
  captchaHash: string;
  errorMessage = 'Invalid Credentials';
  successMessage: string;
  invalidLogin = false;
  loginSuccess = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService) {   }

  ngOnInit() {
    this.drawCaptcha();
  }

  handleLogin() {
    this.authenticationService.authenticationService(this.username, this.password, this.captchaHash, this.captchaText)
      .subscribe((result) => {
      this.invalidLogin = false;
      this.loginSuccess = true;
      this.successMessage = 'Login Successful.';
      this.router.navigate(['/hello-world']);
    }, (e) => {
        if (e.message === 'Wrong captcha!') {
          this.errorMessage = e.message;
        } else {
          this.errorMessage = 'Invalid Credentials';
        }
        this.drawCaptcha();
        this.invalidLogin = true;
        this.loginSuccess = false;
    });
  }

  drawCaptcha() {
    this.authenticationService.captchaService().subscribe((result: any) => {
      this.captchaSrc = `data:image/jpeg;base64,${JSON.parse(result).value}`;
      this.captchaHash = JSON.parse(result).key;
    }, (e) => {
      console.log(e);
    });
  }
}
