const fs = require('fs');

module.exports = {
    generateVersionEnv: (pluginConfig, context) => {
        const { nextRelease, lastRelease } = context;

        let version;
        let isNewRelease = false;

        if (nextRelease) {
            version = nextRelease.version;
            isNewRelease = nextRelease.version !== lastRelease.version;
        } else if (lastRelease) {
            version = lastRelease.version;
        } else {
            version = '1.0.0';
        }

        const content = `VERSION=${version}\nIS_NEW_RELEASE=${isNewRelease}`;

        fs.writeFileSync('version.env', content, 'utf8');
        console.log('version.env file created with content:\n' + content);
    },
};
