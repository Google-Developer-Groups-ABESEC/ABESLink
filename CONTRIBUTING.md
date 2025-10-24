# Contributing

Contributions are welcome! Please open issues or submit pull requests.

## How to Contribute

1. **Fork this repository**  
   Go to the ABESLink repo and click "Fork". A copy will be created on your profile.

2. **Clone your fork**
   ```bash
   git clone https://github.com/<your-username>/ABESLink.git
   ```

3. **Open in Android Studio**  
   Open the project and let Gradle sync. Make sure you have:
   - Android Studio (latest version)
   - JDK 17+
   - Android SDK 24+

4. **Create a new branch**
   ```bash
   git checkout -b fix-issue-name
   ```

5. **Make your changes**  
   Solve the issue or add your feature. Test it on a device/emulator.

6. **Commit your changes**
   ```bash
   git add .
   git commit -m "fix: short description of your change"
   ```

7. **Push to your fork**
   ```bash
   git push origin fix-issue-name
   ```

8. **Open a Pull Request**  
   Go to the original repository and open a PR with a clear title and description.

## PR Acceptance Criteria

- The code should be clean and follow Kotlin conventions
- Proper description of the change should be given
- No conflicts should be there while merging with main branch
- The app should compile and run without crashes
- Test your changes before submitting

## Commit Message Format

Use clear commit messages:
```
feat: add dark mode toggle
fix: crash when saving empty credentials
docs: update readme
refactor: improve login logic
```

## Found a Bug?

Open an issue with:
- What happened
- What you expected to happen
- Steps to reproduce
- Device info (model, Android version)
- Screenshots if applicable

## Have a Feature Idea?

Open an issue with the `enhancement` label and describe:
- What problem it solves
- How it should work
- Any UI mockups (optional)

## Code Style

- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Keep functions small and focused
- We use MVVM architecture - check existing code for reference

## Questions?

- Open a discussion on GitHub
- Email us: gdg.abesec@gmail.com
- Check existing issues first

## Security Issues

Found a security vulnerability? Email us at gdg.abesec@gmail.com instead of opening a public issue.

---

Thanks for contributing! Every PR helps make ABESLink better for the ABES community.

Made with ❤️ by GDG ABES
