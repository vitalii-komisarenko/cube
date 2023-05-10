import qualified Data.Text as T

main = do
    input <- getContents
    {-
    https://www.iso-9899.info/n1570.html
    5.1.1.2 1.2
    Each instance of a backslash character (\) immediately followed by a new-line
    character is deleted, splicing physical source lines to form logical source lines.
    Only the last backslash on any physical source line shall be eligible for being part
    of such a splice. A source file that is not empty shall end in a new-line character,
    which shall not be immediately preceded by a backslash character before any such
    splicing takes place.
    -}

    -- \ at the end of file is not permitted
    -- It is easier just to remove it
    let tmp1 = if last input == '\\' then init input else input

    -- Remove \r characters to simplify handling of different line breaks,
    -- such as \r\n, \n\r and \n
    let tmp2 = T.replace (T.pack "\r") (T.pack "") (T.pack tmp1)

    -- Remove \ followed by \n
    let output = T.replace (T.pack "\\\n") (T.pack "") tmp2

    -- Use putStrLn to ensure the output ends with a newline
    putStrLn (T.unpack output)
