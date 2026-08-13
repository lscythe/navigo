# WIP

## Git hooks

Commits are formatted and validated by [kempt](https://github.com/ZacSweers/kempt) (Kotlin/whitespace formatting, Apache license headers) and [hk](https://github.com/jdx/hk) (git hook orchestration, conventional commit messages), both managed by [mise](https://mise.jdx.dev).

Install mise, then from the repo root:

```sh
mise install
hk install --mise
```

This registers `pre-commit` (runs `kempt`) and `commit-msg` (checks conventional commit format) for this repo. Config lives in `mise.toml`, `hk.pkl`, and `.kempt.toml`.