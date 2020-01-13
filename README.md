# image-utils

Imaging utilities released by the Parker Institute for Cancer Immunotherapy.

## Installation

Download from (releases)[https://github.com/ParkerICI/image-utils/releases].

## Usage

`$ java -jar image-utils-0.1.0-standalone.jar [command] [args]`

If you are working with large images, you may need to increase the maximum heap size.

`java -Xmx16G -jar image-utils-0.1.0-standalone.jar [command] [args]`

## Commands

### split-hyperstack

`$ java -jar image-utils-0.1.0-standalone.jar split-hyperstack [args]`

Splits a tiff hyperstack into individual tiffs. Accepts the following options:

`-i` or `--input`
Required. Either a tiff file or a directory containing multiple tiff files to be split.

`-o` or `--outpath`
Optional. A folder to write the output files to. Will create if it does not exist. Defaults to the parent folder of the tiff being split.

`-s` or `--subfolder`
Optional. If used, generates a folder for each tiff being split and puts the individual tiffs in the subfolder instead of alongside the parent tiff.

### help

Outputs help and usage information.

