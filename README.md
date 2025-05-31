# 🧪 Naukri Profile Resume Auto-Updater (Java + Selenium + TestNG + Mavan + Github Action)

This project automatically logs into [Naukri.com](https://www.naukri.com/), navigates to your profile, and uploads your latest resume every day at **9:00 AM IST** using **GitHub Actions**. It uses a headless Chrome browser and is built with Java + Selenium + TestNG.

---

## 📌 Features

- 🔒 Secure login using GitHub Secrets (no hardcoded credentials)
- 📤 Automatically uploads your resume PDF
- 🤖 Runs daily using GitHub Actions (CRON schedule)
- 💻 Headless browser testing using Selenium and ChromeDriver
- 📸 Takes screenshots on failure (optional feature)
- 📬 Sends email alerts when the workflow fails

---

## 🛠️ Tech Stack

- **Java 11**
- **Selenium WebDriver**
- **TestNG**
- **Maven**
- **GitHub Actions**
- **ChromeDriver (Headless mode)**

---

## 📁 Project Structure
Naukri_Profile_Automation/
├── src/
│ └── test/
│ └── java/
│ └── com/naukri/automation/NaukriProfileUpdateTest.java
├── resources/
│ └── Deepak_Jena_QA_Resume_2025.pdf
├── .github/
│ └── workflows/
│ └── resume-update.yml
├── pom.xml
├── README.md

---

## 🔐 GitHub Secrets Setup

1. Go to your GitHub repo.
2. Click on **Settings > Secrets and variables > Actions > New repository secret**.
3. Add the following secrets:

| Name             | Value                      |
|------------------|----------------------------|
| `NAUKRI_EMAIL`    | Your Naukri login email    |
| `NAUKRI_PASSWORD` | Your Naukri account password |

---

## 🔄 How It Works

- The GitHub Action runs every day at **9:00 AM IST** (`3:30 AM UTC`).
- It uses Maven to build and run your TestNG test.
- The test opens Naukri.com in a headless Chrome browser, logs in, and uploads your resume.
- The resume file is taken from the `resources` folder in your repo.

---

## 🛡️ Notes

- Chrome runs in **headless mode**, which is compatible with GitHub Actions.
- Resume upload path is handled dynamically: `resources/Deepak_Jena_QA_Resume_2025.pdf`
- Email and password are pulled securely from **GitHub Secrets**.
- You can view logs and rerun failed workflows in the **Actions** tab.

---

## 📬 Email Alerts (Optional)

You can enable **email notifications** for failed workflows:
1. Go to **GitHub > Profile > Settings > Notifications**.
2. Enable **"Email me when workflows fail"**.

---

## 🤝 Contributions

Pull requests are welcome! For major changes, please open an issue first to discuss.

---

## 📄 License

This project is open source and free to use under the MIT License.

---

### 👤 Developed by: **Deepak Jena**
