const { generateVersionEnv } = require('./create-version.js');

module.exports = {
    branches: ["dev"],
    plugins: [
        "@semantic-release/commit-analyzer",
        "@semantic-release/release-notes-generator",
        "@semantic-release/github",
        "@semantic-release/changelog",
        [
            "@semantic-release/git",
            {
                assets: ["CHANGELOG.md", "package.json"],
                message: "chore(release): ${nextRelease.version} [skip ci]\n\n${nextRelease.notes}",
            },
        ],
        {
            analyzeCommits: generateVersionEnv,
            generateNotes: generateVersionEnv,
        },
    ],
};
