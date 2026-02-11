# 🐼 PandTask!

> **Organize your academic life. Boost your productivity.**

**PandTask** is a comprehensive task management application built with **Java** and **JavaFX**. Designed with students in mind, it provides a distraction-free environment to organize academic life and boost productivity.

## 🎯 Project Goals
PandTask was created to address several key challenges:
* **Organization:** Centralizes daily assignments and exams in one intuitive view.
* **Accessibility:** Offers a clean, user-friendly interface for those new to digital planning.
* **Skill Building:** Encourages accountability and proactive time management.
* **Productivity:** Reducing academic stress by keeping all deadlines in one place.

---

## ⚙️ Prerequisites
Before running PandTask, ensure you have the following:
* **Internet Access:** Required for downloading dependencies and initial setup.
* **Java IDE:** We strongly recommend [IntelliJ IDEA](https://www.jetbrains.com/idea/) for the best compatibility.
* **Java Development Kit (JDK):** Version 21 or higher.
* **JavaFX SDK:** Version matching your JDK.

## 📥 Installation & Setup

### 1. Download the JDK
Click [here](https://www.oracle.com/java/technologies/downloads/) to download the JDK.
- Select the **JDK 21** or **JDK 25** tab and download the installer for your OS.
- Run the installer file and follow the on-screen prompts.
- In IntelliJ, go to **File > Project Structure > Project**.
- In the **SDK** dropdown, select the version you just installed. If it does not appear, click **Add SDK > Detect SDKs**.

### 2. Configure JavaFX
Click [here](https://openjfx.io/) to download the JavaFX SDK.
- Download the SDK version that matches your JDK (e.g., SDK 21).
- Extract the downloaded `.zip` file to a safe, permanent location (e.g., `C:\Program Files\Java\`).
- In IntelliJ, go to **File > Project Structure > Libraries**.
- Click the **+** button, select **Java**, and select the `lib` folder inside the folder you just extracted.

### 3. Run Configuration (Crucial Step)
To run the application without errors, you must add VM options:
1.  Go to **Run > Edit Configurations**.
2.  Under **VM Options** (click "Modify Options" if you don't see this field), paste the following line:
    ```bash
    --module-path "PATH_TO_FX" --add-modules javafx.controls,javafx.fxml
    ```
3.  **Important:** Replace `PATH_TO_FX` with the actual address of your JavaFX `lib` folder (e.g., `C:\Program Files\Java\javafx-sdk-21\lib`).

---

## ✨ Key Features

### 🔐 User Management
* **Secure Authentication:** Sign Up and Log In capabilities ensure your data is private.
* **Profile Management:** Update your username or password via the **Settings** menu.
    * *Note: Changing credentials will require a re-login for security.*

### 📅 Task Management
* **Interactive Calendar:** Visualize your workload at a glance.
* **Easy Creation:** simply click the `+` button (top right) to create and personalize new tasks.
* **Quick Tasks:** Use the bottom-left panel for rapid entry of small, immediate to-dos.

### 🎨 Customization & Priority
* **Priority Color-Coding:** Tasks are visually distinct to help you triage immediately:
    * 🔴 **Red:** High Priority
    * 🟡 **Yellow:** Medium Priority
    * 🟢 **Green:** Low Priority
* **Custom Labels:** Organize tasks by subject or category. You can personalize label names and colors clicking  the `+` button on the left panel.

---

## 🛠️ Built With
* **Language:** Java 21+
* **GUI Framework:** JavaFX
* **IDE:** IntelliJ IDEA
* **Data Storage:** CSV (Local File I/O)

---

## 👥 Contributors

- **Paula Com Morales** | Developer | Designer | Documentation Specialist
- **Raymond Huelitl** | Developer | Communication Coordinator | Tester
- **Francisco Espinoza** | Developer | Tester | Documentation Specialist
- **Shahad K Ali** | Developer | Designer | Project Manager

   * *Note: Roles have been changing during the process of making PandTask.*


---
## 🐛 Known Issues
- **Label editing/deleting feature:** The labels cannot be edited or deleted; however it is planned to be implemented in the future.
- **Data Persistence:** Currently, all data is stored locally in `.csv` files. Migration to a SQL database is planned for larger datasets in future updates.
