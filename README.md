# reCaptcha_Java

Java class used to verify reCaptcha response tokens

Usage:

```
ReCaptcha reCaptcha = new ReCaptcha(reCaptchaSecretKey, reCaptchaResponse);
if (!reCaptcha.verify()) {
  System.out.println("reCaptcha check failed");
}
```
