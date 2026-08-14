const path = require("path");

module.exports = (env, argv) => {
    const isProduction = argv.mode === "production";

    return {
        entry: "./src/js/main.js",
        output: {
            filename: "main.js",
            path: path.resolve(__dirname, "../src/main/resources/static/dist"),
            clean: true,
        },
        devtool: isProduction ? "source-map" : "eval-source-map",
        module: {
            rules: [
                {
                    test: /\.scss$/,
                    use: [
                        "style-loader",
                        "css-loader",
                        {
                            loader: "sass-loader",
                            options: {
                                sassOptions: { silenceDeprecations: ["import", "global-builtin", "color-functions"] },
                            },
                        },
                    ],
                },
                {
                    test: /\.css$/,
                    use: ["style-loader", "css-loader"],
                },
                {
                    test: /\.svg$/,
                    type: "asset/source",
                },
            ],
        },
    };
};
