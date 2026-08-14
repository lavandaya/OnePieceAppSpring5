const js = require("@eslint/js");
const globals = require("globals");

module.exports = [
    js.configs.recommended,
    {
        files: ["src/**/*.js"],
        languageOptions: {
            ecmaVersion: "latest",
            sourceType: "module",
            globals: {
                ...globals.browser,
            },
        },
        rules: {
            "no-unused-vars": "warn",
            "no-console": ["warn", { allow: ["error", "warn"] }],
            eqeqeq: "error",
        },
    },
    {
        files: ["webpack.config.cjs", "eslint.config.cjs"],
        languageOptions: {
            ecmaVersion: "latest",
            sourceType: "commonjs",
            globals: {
                ...globals.node,
            },
        },
    },
    {
        ignores: ["dist/**", "node_modules/**", "../src/main/resources/static/dist/**"],
    },
];
