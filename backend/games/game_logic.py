"""Pure game logic — no Django dependencies."""

WINNING_COMBINATIONS = [
    (0, 1, 2), (3, 4, 5), (6, 7, 8),  # rows
    (0, 3, 6), (1, 4, 7), (2, 5, 8),  # columns
    (0, 4, 8), (2, 4, 6),              # diagonals
]


def check_winner(board: str):
    """Return 'X', 'O', 'draw', or None if still in progress."""
    for a, b, c in WINNING_COMBINATIONS:
        if board[a] != ' ' and board[a] == board[b] == board[c]:
            return board[a]
    if ' ' not in board:
        return 'draw'
    return None


def apply_move(board: str, position: int, symbol: str) -> str:
    cells = list(board)
    cells[position] = symbol
    return ''.join(cells)


def is_valid_move(board: str, position: int) -> bool:
    return 0 <= position <= 8 and board[position] == ' '
