let $board;
let $target;
let $destination;
let $path;
let $status;

const $boardFrame = document.getElementById("board-frame");
const $black = document.getElementById("black");
const $white = document.getElementById("white");
const $blackScore = document.getElementById("black-score");
const $whiteScore = document.getElementById("white-score");

createBoard();

function getFetch(url) {
    return fetch(url).then(response => response.json());
}

function postFetchMove(url) {
    return fetch(url, {
        method: 'post',
        headers: {
            'Content-type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            "target": $target,
            "destination": $destination
        })
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
    });
}

async function postFetchPath(url) {
    return fetch(url, {
        method: 'post',
        headers: {
            'Content-type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            "target": $target,
        })
    }).then(response => {
        if (response.ok) {
            return response.json();
        }
    });
}

async function moveBoard() {
    await postFetchMove("move").then(data => {
        $board = data;
        clearBoard();
        refreshBoard();
    });
}

async function findPath() {
    clearMovablePosition();

    await postFetchPath("movable").then(data => {
        $path = data;
    });
    console.log($path);
    showMovablePosition();
}

function showMovablePosition() {
    $path.forEach(path => {
        let tile = document.getElementById(path);
        tile.classList.add("rainbow-bg");
    });
}

async function findScore() {
    await postFetchPath("score").then(data => {
        $status = data;
    });
    console.log($status + ", black = " + $status.blackScore + ", white = " + $status.whiteScore);
    showScore();
}

function showScore() {
    $blackScore.innerHTML = `
        <label>${$status.blackScore}</label>
    `;
    $whiteScore.innerHTML = `
        <label>${$status.whiteScore}</label>
    `;
}

function clearMovablePosition() {
    if ($path == null) {
        return;
    }

    $path.forEach(path => {
        let tile = document.getElementById(path);
        tile.classList.remove("rainbow-bg");
    });

    $path = null;
}

async function createBoard() {
    clearBoard();
    await getFetch("create").then(data => {
        $board = data;
    })
    refreshBoard();
}

function clearBoard() {
    const $pieces = $boardFrame.querySelectorAll('.piece');
    $pieces.forEach(piece => {
        piece.remove();
    });
}

function refreshBoard() {
    if ($board.end == "true") {
        confirm($board.turn + "(이)가 승리했습니다!!");
        createBoard();
        return;
    }
    changeTurn($board.turn);
    findScore();
    let boardKeys = Object.keys($board.board);
    let boardSize = boardKeys.length;
    for (let i = 0; i < boardSize; i++) {
        let tileData = document.getElementById(boardKeys[i]);
        if ($board.board[boardKeys[i]].name != null) {
            tileData.innerHTML = `
            <img class="piece" src="../images/${$board.board[boardKeys[i]].team.toLowerCase()}-${$board.board[boardKeys[i]].name.toLowerCase()}.png"
             width="80px" height="80px">
            `;
        }
    }
}

function setTarget(e) {
    try {
        if (!e.target.id) {
            $target = e.target.closest(".tile").id;
            findPath($target);
        }
    } catch (e) {
        console.log("체스말을 선택해야합니다.");
    }
}

function setDestination(e) {
    clearMovablePosition();

    try {
        if (!e.target.id) {
            $destination = e.target.closest(".tile").id;
            move();
            return;
        }
        $destination = e.target.id;
        move();
    }
    catch (e) {
        console.log("체스말을 선택해야합니다.");
    }
}

function createMoveCommand(e) {
    if ($target == null) {
        setTarget(e);
        return;
    }
    setDestination(e);
}

function move() {
    console.log("이동! 타겟: " + $target + ", 목적지: " + $destination);
    moveBoard();
    $target = null;
    $destination = null;
}

function changeTurn(team) {
    console.log(team)
    if (team == "BLACK") {
        $black.classList.remove("turn");
        $white.classList.add("turn");
        return;
    }
    $black.classList.add("turn");
    $white.classList.remove("turn");
}

document.addEventListener("click", createMoveCommand);