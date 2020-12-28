#!/usr/bin/perl

use strict;
use warnings;

my $basePath =
  "target/generated-sources/openapi/src/main/java/de/bckx/backend-service/dto/";

while ( my $file = glob $basePath . '*DTO.java' ) {
    print "Processing File: " . $file . "\n";
    open( my $fh, '<', $file );
    my @lines = <$fh>;
    close $fh;

    open( $fh, '>', $file );

    foreach my $line (@lines) {
        print $fh $line;
    }
    close $fh;
}
