import unittest
import io
import tempfile
from contextlib import redirect_stdout
from pathlib import Path
from unittest.mock import patch

from classify_bot_pr import classify, is_translation_path


class ClassifyBotPullRequestTest(unittest.TestCase):
    def test_translation_only_weblate_pull_request(self):
        self.assertTrue(classify("app/hosted-weblate", ["id.tbx"]))
        self.assertTrue(
            classify(
                "app/hosted-weblate",
                ["core/resources/src/commonMain/composeResources/values-id/strings.xml"],
            )
        )

    def test_mixed_weblate_pull_request_uses_normal_ci(self):
        self.assertFalse(
            classify("app/hosted-weblate", ["id.tbx", "app/src/main/kotlin/Main.kt"])
        )

    def test_other_authors_use_normal_ci(self):
        self.assertFalse(classify("dependabot[bot]", ["id.tbx"]))
        self.assertFalse(classify("lscythe", ["id.tbx"]))

    def test_empty_change_list_fails_closed(self):
        self.assertFalse(classify("app/hosted-weblate", []))

    def test_allowlist_rejects_untrusted_paths(self):
        rejected = [
            "build.gradle.kts",
            ".github/workflows/ci.yml",
            "README.md",
            "arbitrary.xml",
            "core/resources/src/commonMain/composeResources/strings.xml",
            "../id.tbx",
        ]
        for path in rejected:
            with self.subTest(path=path):
                self.assertFalse(is_translation_path(path))

    def test_cli_reads_changed_files_without_shell_expansion(self):
        with tempfile.TemporaryDirectory() as directory:
            paths_file = Path(directory) / "changed-files"
            paths_file.write_text("id.tbx\n")
            output = io.StringIO()
            with patch(
                "sys.argv",
                [
                    "classify_bot_pr.py",
                    "--author",
                    "app/hosted-weblate",
                    "--changed-files-file",
                    str(paths_file),
                ],
            ), redirect_stdout(output):
                from classify_bot_pr import main

                main()

            self.assertEqual(
                "weblate_translation_only=true\n",
                output.getvalue(),
            )

if __name__ == "__main__":
    unittest.main()

