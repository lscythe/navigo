# Contributing to Navigo

Thank you for contributing. Keep changes focused, documented, and verifiable.

## Before opening a pull request

1. Check existing issues and pull requests.
2. Link the relevant external FSD, TSD, or design decision.
3. Create a focused feature branch from `develop`.
4. Keep secrets, credentials, and private data out of commits.

## Branch flow

```text
feature/* → develop → release/* → main
```

Release branches are created from `develop`. After stabilization, merge the release branch into `main`, then merge it back into `develop` so release fixes remain in ongoing development.

- `feature/*`: implementation work, branched from `develop`
- `develop`: integration branch
- `release/*`: release stabilization, branched from `develop`
- `main`: production-ready code

Protected branches require pull requests and review. Do not force-push or delete protected branches.

## Pull requests

Use the pull request template. Include:

- A concise summary and rationale
- Related issue(s)
- External specification links
- Acceptance criteria
- Verification performed
- Screenshots for relevant UI changes

Keep unrelated refactors out of feature pull requests.

## Local verification

Use the repository Gradle wrapper and the JDK version required by the project. At minimum, run the checks relevant to your change. CI runs build, tests, coverage, dependency review, and CodeQL checks.

## Commit and review expectations

- Do not commit generated build output, credentials, or local configuration.
- Resolve review conversations before requesting final approval.
- Update the pull request when behavior or scope changes.
- Keep public interfaces and user-visible behavior clear and maintainable.
