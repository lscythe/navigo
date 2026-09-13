import argparse
import xml.etree.ElementTree as ElementTree
from pathlib import Path

from classify_bot_pr import is_translation_path


def _root_name(path: Path) -> str:
    try:
        return ElementTree.parse(path).getroot().tag.rsplit("}", 1)[-1]
    except (ElementTree.ParseError, OSError) as error:
        raise ValueError(f"Invalid XML in {path}: {error}") from error


def validate_xml(path: Path) -> None:
    if _root_name(path) != "resources":
        raise ValueError(f"Compose resource must use a <resources> root: {path}")


def validate_tbx(path: Path) -> None:
    if _root_name(path) not in {"martif", "tbx"}:
        raise ValueError(f"TBX glossary must use a <martif> or <tbx> root: {path}")


def validate_paths(manifest_path: Path) -> None:
    lines = manifest_path.read_text(encoding="utf-8").splitlines()
    if not lines:
        raise ValueError("Changed-file list is empty")
    for line in lines:
        original, separator, data_path = line.partition("\t")
        if not separator or not is_translation_path(original):
            raise ValueError(f"Invalid translation manifest entry: {line}")
        path = Path(data_path)
        if not path.is_file():
            raise ValueError(f"Translation data is missing: {original}")
        if original == "id.tbx":
            validate_tbx(path)
        else:
            validate_xml(path)


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("manifest_file", type=Path)
    args = parser.parse_args()
    validate_paths(args.manifest_file)
    print(f"Validated {len(args.manifest_file.read_text(encoding='utf-8').splitlines())} translation file(s)")


if __name__ == "__main__":
    main()
