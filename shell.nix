with (import <nixpkgs> {});

mkShell {
  buildInputs = [
    jdk17_headless
    sbt
  ];
  shellHook = ''
  '';
}
