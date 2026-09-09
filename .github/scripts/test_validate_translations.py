import tempfile
import unittest
from pathlib import Path

from validate_translations import validate_paths


class ValidateTranslationsTest(unittest.TestCase):
    def test_valid_compose_resource_xml(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            data = root / "strings.xml"
            data.write_text('<resources><string name="hello">Halo</string></resources>')
            manifest = root / "manifest"
            manifest.write_text(f"core/resources/src/commonMain/composeResources/values-id/strings.xml\t{data}\n")
            validate_paths(manifest)

    def test_malformed_or_wrong_root_compose_xml(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            data = root / "strings.xml"
            manifest = root / "manifest"
            for content in ("<resources>", "<catalog />"):
                data.write_text(content)
                manifest.write_text(f"core/resources/src/commonMain/composeResources/values-id/strings.xml\t{data}\n")
                with self.subTest(content=content), self.assertRaises(ValueError):
                    validate_paths(manifest)

    def test_valid_tbx(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            data = root / "id.tbx"
            data.write_text('<martif type="TBX"><text><body /></text></martif>')
            manifest = root / "manifest"
            manifest.write_text(f"id.tbx\t{data}\n")
            validate_paths(manifest)

    def test_malformed_or_wrong_root_tbx(self):
        with tempfile.TemporaryDirectory() as directory:
            root = Path(directory)
            data = root / "id.tbx"
            manifest = root / "manifest"
            for content in ("<martif>", "<resources />"):
                data.write_text(content)
                manifest.write_text(f"id.tbx\t{data}\n")
                with self.subTest(content=content), self.assertRaises(ValueError):
                    validate_paths(manifest)

    def test_unknown_and_empty_paths_fail_closed(self):
        with tempfile.TemporaryDirectory() as directory:
            manifest = Path(directory) / "manifest"
            manifest.write_text("")
            with self.assertRaises(ValueError):
                validate_paths(manifest)
            manifest.write_text("build.gradle.kts\tmissing\n")
            with self.assertRaises(ValueError):
                validate_paths(manifest)


if __name__ == "__main__":
    unittest.main()
