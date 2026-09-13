import argparse
import re

WEBLATE_AUTHOR = "app/hosted-weblate"
_TRANSLATION_PATH = re.compile(
    r"^(?:core|feature|app)/[^/]+(?:/[^/]+)*/src/[^/]+/composeResources/values(?:-[a-zA-Z0-9-]+)?/strings\.xml$"
)


def is_translation_path(path: str) -> bool:
    normalized = path.replace("\\", "/")
    if normalized == "id.tbx":
        return True
    if normalized.startswith("/") or ".." in normalized.split("/"):
        return False
    return _TRANSLATION_PATH.fullmatch(normalized) is not None


def classify(author: str, changed_files: list[str]) -> bool:
    return (
        author == WEBLATE_AUTHOR
        and bool(changed_files)
        and all(is_translation_path(path) for path in changed_files)
    )
def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--author", required=True)
    parser.add_argument("--changed-file", action="append", default=[])
    parser.add_argument("--changed-files-file")
    args = parser.parse_args()
    changed_files = list(args.changed_file)
    if args.changed_files_file:
        with open(args.changed_files_file, encoding="utf-8") as paths_file:
            changed_files.extend(
                line.rstrip("\n") for line in paths_file if line.strip()
            )
    translation_only = classify(args.author, changed_files)
    print(f"weblate_translation_only={str(translation_only).lower()}")


if __name__ == "__main__":
    main()
