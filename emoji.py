from texteditor.api import TextModificationHandler
# Emoji insertion Python script. 

class EmojiHandler(TextModificationHandler):
    last_three_chars = ""

    def onTextModified(self, prev, current):
        api.replaceText(":-)", u"\U0001f60a")

api.registerTextModificationHandler(EmojiHandler())