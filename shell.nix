with (import <nixpkgs> {});

mkShell {
  buildInputs = [
    graalvm17-ce
    sbt
  ];
  shellHook = ''
  '';
}
